# 여행객들을 위한 웹서비스

시연영상  
https://play-tv.kakao.com/embed/player/cliplink/438946006?service=daum_tistory



## 제작동기

프로젝트 이름과 같이 For your travel (당신의 여행을 위한) 웹 서비스를 제작하고자 하였습니다.

여행객들이 저희 서비스를 통해 관광지 정보와 다른 사용자들의 여행후기를 참고하여 성공적인 여행계획을 완성하는데 도움을 받을 수 있습니다.

## 사용기술

-   Vue.js
    -   vuex
    -   vuex-persistedstate
    -   axios
    -   vue-router
    -   vue-wordcloud
    -   jwt-decode
-   SpringBoot
    -   myBatis
    -   jjwt
    -   spring-boot-starter-mail
-   MySql

## 구현기능

-   사용자 프로필
-   팔로우, 팔로잉
-   로그인, 로그아웃, 회원가입 (jwt)
-   게시판 CRUD, 댓글 CRUD
-   회원정보 수정

## 1\. 로그인, 로그아웃, 회원가입 (jwt, vuex, vuex-persistedstate)

### 1-1. 회원가입

#### SpringBoot

-   회원가입 Controller

```

@RestController
@RequestMapping("/user")
@Api("사용자 컨트롤러  API V1")
@CrossOrigin("*")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${file.path}")
    private String uploadPath;

    @Value("${file.imgPath}")
    private String uploadImgPath;

    private UserService us;
    private JwtService js;
    private EmailService es;

    @Autowired
    public UserController(UserService us, JwtService js,EmailService es) {
        super();
        this.us = us;
        this.js = js;
        this.es = es;
    }

    ...

    @ApiOperation(value = "회원가입", notes = "회원가입(이미지O)", response = Map.class)
    @PostMapping("/join-form")
    public ResponseEntity<Map<String, Object>> join(
            @ModelAttribute @ApiParam(value = "회원가입할 회원의 정보", required = true) UserDto userDto,
            HttpServletRequest request) throws IllegalStateException, IOException {

        MultipartFile file = userDto.getFile();
        System.out.println(file);
        logger.debug("join user : {} ", userDto);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        if (!file.isEmpty()) {
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadPath + File.separator + today;
            logger.debug("저장 폴더 : {}", saveFolder);
            File folder = new File(saveFolder);
            if (!folder.exists())
                folder.mkdirs();
            String originalFileName = file.getOriginalFilename();
            if (!originalFileName.isEmpty()) {
                String saveFileName = UUID.randomUUID().toString()
                        + originalFileName.substring(originalFileName.lastIndexOf('.'));
                userDto.setSfolder(today);
                userDto.setOfile(originalFileName);
                userDto.setSfile(saveFileName);
                logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", file.getOriginalFilename(), saveFileName);
                file.transferTo(new File(folder, saveFileName));
            }
        }

        try {
            us.joinUser(userDto);
            resultMap.put("code", "200");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            logger.error("정보조회 실패 : {}", e);
            resultMap.put("code", "500");
            status = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    @ApiOperation(value = "회원가입", notes = "회원가입(이미지X)", response = Map.class)
    @PostMapping("/join-json")
    public ResponseEntity<Map<String, Object>> joinJson(@RequestBody @ApiParam(value = "회원가입할 회원의 정보", required = true) UserDto userDto,
            HttpServletRequest request) throws IllegalStateException, IOException {
        logger.debug("join user : {} ", userDto);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            userDto.setOfile("noimg.png");
            userDto.setSfile("noimg.png");
            userDto.setSfolder("noimg");
            us.joinUser(userDto);
            resultMap.put("code", "200");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            logger.error("정보조회 실패 : {}", e);
            resultMap.put("code", "500");
            status = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    ...
```

회원가입 진행 시 프로필 이미지를 넣는 경우와 넣지 않는 경우를 처리하기 위해 `/join-form`과 `/join-json` 두 개의 컨트롤러로 구분하였습니다.

-   회원가입 service

```
import java.security.MessageDigest;
import java.security.SecureRandom;


@Service
public class UserServiceImpl implements UserService {

    private static final int SALT_SIZE = 16;
    private UserMapper UserMapper;

    public UserServiceImpl(UserMapper UserMapper) {
        super();
        this.UserMapper = UserMapper;
    }

    @Override
    public void joinUser(UserDto UserDto) throws Exception {
        String salt = makeSalt();
        UserDto.setPassword(hashing(UserDto.getPassword(),salt));
        UserDto.setSalt(salt);
        UserMapper.joinUser(UserDto);
    }

    private String makeSalt() throws Exception {// salt값 생성
        SecureRandom random = new SecureRandom();
        byte[] temp = new byte[SALT_SIZE];
        random.nextBytes(temp);

        return byteToString(temp);
    }

    // 비밀번호 해싱
    private String hashing(String password, String salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");// SHA-256 해시함수를 이용
        for (int i = 0; i < 10000; i++) {// salt를 합쳐 새로운 해시비밀번호를 생성해 디코딩를 방지
            String temp = password + salt;// 패스워드와 Salt를 합쳐 새로운 문자열 생성
            md.update(temp.getBytes());// temp의 문자열을 해싱하여 md에 저장
            password = byteToString(md.digest());// md객체의 다이제스트를 얻어 password를 갱신
        }
        return password;
    }

    private String byteToString(byte[] temp) {// 바이트 값을 16진수로 변경
        StringBuilder sb = new StringBuilder();
        for (byte a : temp) {
            sb.append(String.format("%02x", a));
        }
        return sb.toString();
    }
```

`import java.security.MessageDigest` `import java.security.SecureRandom` 라이브러리를 이용하여 비밀번호를 암호화 하여 db에 저장하였습니다.

또한 mybatis를 활용하여 db에 접근하였습니다.

> MyBatis

-   Java Object와 SQL문 사이의 자동 Mapping 기능을 지원하는 ORM Framework
-   MyBatis는 SQL을 별도의 파일(XML)로 분리해서 관리
-   자바와 SQL을 연결만 해준다!
-   JAVA - mybatis - MySQL

---

#### Vue

-   formData를 활용하여 이미지를 서버로 post 하기 위해서 header의 `Content-Type`을 `multipart/form-data`로 설정해줬습니다

```
const formData = new FormData();
      formData.append("userId", this.user.userId);
      formData.append("userName", this.user.userName);
      formData.append("password", this.user.password);
      formData.append("email", this.user.email);
      formData.append("file", this.user.file);
      axios({
        method: 'post',
        url: `${process.env.VUE_APP_BASE_URL}user/join-form`,
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        data: formData
      })
        .then(({ data }) => {
          if (data.code === "200") {
            alert("회원가입이 완료되었습니다.");
            this.$router.push({ name: "home" });
          }
        })
        .catch(() => alert("잠시 후 시도해 주세요"));
    }
```

### 1-2. 로그인, 사용자인증

#### SpringBoot

-   로그인 Controller

```
@ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호)", required = true) UserDto userDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            UserDto loginUser = us.loginUser(userDto);
            if (loginUser != null) {
                JwtResponseDTO jrdto = new JwtResponseDTO();
                jrdto.setAuthority(loginUser.getAuthority());
                jrdto.setUserId(loginUser.getUserId());
                jrdto.setUserNo(loginUser.getUserNo());
                String accessToken = js.createAccessToken("user", jrdto);// key, data
                String refreshToken = js.createRefreshToken("user", jrdto);// key, data
                us.saveRefreshToken(loginUser.getUserNo(),refreshToken);
                logger.debug("로그인 accessToken 정보 : {}", accessToken);
                logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);
                resultMap.put("code", "200");
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("code", "401");
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("로그인 실패 : {}", e);
            resultMap.put("code", "500");
            status = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
```

우선 service단에 로그인 요청을 보냅니다.

> 로그인 요청을 받는 service단

```
@Override
public UserDto loginUser(UserDto loginUser) throws Exception {
    String userId = loginUser.getUserId();
    String salt = UserMapper.getSalt(userId);
    loginUser.setPassword(hashing(loginUser.getPassword(),salt));
    return UserMapper.loginUser(loginUser);
}
```

클라이언트에서 넘겨준 비밀번호와 db에 저장되었는 salt값을 이용하여 비밀번호를 해싱해줬습니다. (db의 password컬럼에는 암호화된 데이터가 저장되어 있기 때문에)  
해싱된 비밀번호로 업데이트된 loginUser dto를 mapper에 전달하여 로그인을 진행하게 됩니다.

---

JwtResponseDto 를 JwtService(`import io.jsonwebtoken.Claims` `import io.jsonwebtoken.Jws` `import io.jsonwebtoken.Jwts` `import io.jsonwebtoken.SignatureAlgorithm`를 이용하고 있는 Service클래스)에 넘겨줘서 refreshToken과 accessTokenn을 생성하게 됩니다.  
이렇게 생성된 token은 클라이언트에게 response로 넘겨줍니다.

#### Vue

-   로그인

`TheLogin.vue`

```
...mapActions(userStore, ["userConfirm", "getUserInfo"]),
    login() {
      if (this.saveid) {
        this.setId(this.user.userId);
      } else {
        this.deleteId();
      }
      let msg = "";
      if (!this.user.userId || this.userId === "") {
        msg = "아이디를 입력해주세요.";
      }

      if (!this.user.password || this.user.password === "") {
        msg = "비밀번호를 입력해주세요.";
      }

      if (msg !== "") {
        alert(msg);
        return;
      }
      this.userConfirm(this.user);
    }
```

vuex의 userConfirm action에 입력받은 user객체를 넘겨줍니다.

`userStore.js` (vuex)

```
userConfirm({ commit, dispatch }, user) {
      axios({
        method: "post",
        url: `${process.env.VUE_APP_BASE_URL}user/login`,
        data: user,
      })
        .then(({ data }) => {
          if (data.code === "200") {
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            // console.log("login success token created!!!! >> ", accessToken, refreshToken);
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            commit("SET_IS_VALID_TOKEN", true);
            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);
            dispatch("getUserInfo");
            alert("로그인 성공 :)");
            router.push({ name: "home" }).catch(() => {});
          } else {
            alert("로그인 실패! 비밀번호를 확인해주세요");
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
```

로그인 성공 시 state값과 token값을 sessionStorage에 저장합니다. 또한 사용자 정보를 갱신하기 위해 `getUserInfo` action을 수행합니다.

`vuex-persistedstate` 라이브러리를 통해 화면 새로고침 시에도 vuex의 store값이 사라지지 않도록 처리했습니다.

## 2\. 사용자 프로필

#### Vue

반응형으로 프로필의 내용이 바뀌도록 하기위해서 computed와 watch를 사용했습니다.

```
computed: {
    ...mapState(userStore, ["isLogin", "userInfo", "followings"]),
    userImgSrc() {
      return `${process.env.VUE_APP_BASE_URL}file/download/${this.profileInfo.sfolder}/${this.profileInfo.ofile}/${this.profileInfo.sfile}`;
    },
    userId() {
      return this.$route.params.userId;
    }, // param으로 userId를 가져옴
    followerCnt() {
      return this.follower.length;
    },
    followingCnt() {
      return this.following.length;
    },
  },
  watch: {
    userId() {
      axios
        .get(`${process.env.VUE_APP_BASE_URL}user/info/${this.userId}`)
        .then(({ data }) => {
          this.profileInfo = data.userInfo;

          axios
            .get(
              `${process.env.VUE_APP_BASE_URL}travel-review/list-user/${this.profileInfo.userNo}`
            )
            .then(({ data }) => {
              this.travelReview = data.travelReview;
            })
            .catch(() => {
              console.log("review 가져오는 중 에러 발생");
            });

          axios
            .get(
              `${process.env.VUE_APP_BASE_URL}travel-plan/planlist-preview/${this.profileInfo.userNo}`
            )
            .then(({ data }) => {
              this.plans = data.planlist;
            })
            .catch(() => {
              console.log("planlist 가져오는 중 에러 발생");
            });

          axios
            .get(`${process.env.VUE_APP_BASE_URL}follow/follower/${this.profileInfo.userNo}`)
            .then(({ data }) => {
              this.follower = data.follower;
            })
            .catch(() => {
              console.log("follower 가져오는 중 에러 발생");
            });

          axios
            .get(`${process.env.VUE_APP_BASE_URL}follow/following/${this.profileInfo.userNo}`)
            .then(({ data }) => {
              this.following = data.following;
            })
            .catch(() => {
              console.log("following 가져오는 중 에러 발생");
            });

          if (this.profileInfo.userNo === this.userInfo.userNo) {
            // 내 프로필인 경우
            this.notMe = false; // 내가 맞으므로 notMe = false
            this.followBtn = false; // follow 버튼 보이면 안됨
          } else if (this.followings.includes(this.profileInfo.userNo)) {
            this.notMe = true; // 내가 아님, notMe = true
            this.followBtn = false; // follow버튼 false
          } else {
            this.followBtn = true; // 팔로우 버튼 보여지고,
            this.notMe = false; // 내가 아님
          }
        })
        .catch(({ err }) => {
          console.log(err);
        });
    },
  },
```

1\. 라우터의 param으로 userId를 넘겨 받는데, 이 userId를 computed에 등록

> computed는 반응형 getter라고 생각하면 된다. 즉 this.$route.params.userId 를 감시하고 있다가 값이 변경되면 userId를 다시 계산한다.

2\. computed에 등록한 userId를 watch에 등록

> computed가 새 프로퍼티를 생성하고 그것의 getter 로 익명함수를 설정되는 것과는 달리 watch는 아무 프로퍼티도 생성하지 않고 익명함수는 단순히 콜백함수로의 역할을 한다. watch에 명시된 프로퍼티는 감시할 대상을 의미할 뿐이다.

3\. 사용자 정보를 제대로 받아오면 여행리뷰, 여행계획, 팔로워, 팔로우 정보를 axios하도록 코드를 작성
