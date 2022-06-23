# projectClone5-BE

# W1_mountain_review

## 항해99  클론프로젝트 (참가자 : 홍수민, 김진희, 노흥진, 함형준, 이정우)

## 제작 기간 & 팀원 소개
- 2022.06.17 ~ 2022.06.23
- Front-end : 홍수민 , 김진희
- Back-end : 노흥진 , 함형준 , 이정우 

## 플랫폼소개
<!-- 
<a href="" target="_blank">프로젝트 페이지 바로가기</a>
 -->
 
<br>
<hr>
<br>


 <br>
 <br>
 
## 사용 기술 및 라이브러리
### Front-end
- Javascript
- Axios
- React-redux
- Redux-thunk
- React-router-dom
- Styled-component
- connected-react-router
- lodash
- react-icons

### Back-end
- java8
- spring JPA
- mysql
- spring 2.7

### 배포
- AWS S3

 <br>
 <br>
 
## 실행 화면


 <br>
 <br>
 
## 핵심기능
### JWT Token을 활용한 사용자 인증
- JWT 토큰을 이용하여 로그인 기능 및 인증 기능 구현
- 회원가입 및 로그인 시 중복확인 가능
### 이미지 업로드 기능
- 이미지 url을 사용하여 이미지 업로드
### 게시글 및 댓글 등록/수정/삭제 기능
- 게시글 등록 및 수정, 삭제 기능
- 게시글 내 댓글 등록 및 수정, 삭제 기능
- 로그인된 사용자만 게시글 및 댓글 등록/수정/삭제 가능

<br>
<br>

## 관련 링크
https://www.frip.co.kr/
<!-- 
## 프로젝트 정보

 - 개발기간 : 22.6.10-6.16
---
## Stack

 - frontend : HTML, CSS, javascript

 - backend : Java , spring

 - db : mySql

 - react.js 
<!-- ---
## 결과물

---
## 상세기능
### 메인페이지
1. 메인
 - 게시글 전체 조회
<!--  - modal 기능 
 - 사진 클릭 시 상세페이지
 - 검색기능 가능
<!--  - [전체목록 보기] button click 시 이벤트 발생  -->
2. modal 
 - 등록하기 button click시 이벤트 발생
 - 사진 클릭 시 상세페이지에 작성했던 정보 모달로 call -->

### 로그인,회원가입

 - 메인 페이지에서 로그인 페이지로 rendering 
 - JWT Token 이용 
<!--  - 회원가입 button 클릭 시 회원가입 button으로 toggle 
 - Bulma 사용 -->

### 등록페이지
1. 등록 종류
 - Url 등록, 제목, 내용 
<!--  - 사진 업로드 시 파일명 명시 -->
<!--  - 개선점 : 어떤 사진을 올렸는지 확인하기 위해 파일명을 명시하였지만 사진파일을 미리보기를 추가하여 사용자의 실수를 줄일 수 있을거 같다. -->
<!-- 2. 예외처리
 - 사진 또는 산이름을 미작성시 경고알람
 - 개선점 : 산이름, 코스 이름, 지역명, 소감을 작성시 욕설 또는 은어가 들어가는 경우를 예외처리 (자연어처리기능)  -->

### 게시물

 - 등록페이지로부터 받은 데이터를 메인페이지에 게시물로 나타냄
 - 게시물의 경우 사진만 보여지며 마우스가 사진으로 이동시 이벤트가 발생하여 산이름, 작성자, 편의시설이 보여짐
 - 사진 클릭시 사진, 산이름, 위치, 편의시설, 소감의 데이터가 모두 보여짐 (modal 기능 구현)
---
 
## API 구성

### 로그인 api  (JWT)

<!-- *> - 메인페이지인 index.html 파일과 등록된 게시글의 정보를 mongoDB에서 가져와 rendering 해준다.* -->

```java
//로그인시 아이디 확인
    @PostMapping("/api/user/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        if (memberService.login(loginRequestDto)) {
            String token = jwtTokenProvider.createToken(loginRequestDto.getUsername());
            System.out.println(token);
            return token;
        } else {
            return "닉네임 또는 패스워드를 확인해주세요";
        }
    }
```   

```java
    // JWT 토큰 생성
    public String createToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }
```   

```java
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
```   

*> 로그인페이지 로그인 쿠키가 만료/ 존재하지 않을때 처리*

```java
    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
```

*> 토큰 인증 정보 조회 , 토큰에서 회원정보 추출*

```java
    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
```


*> - 로그인시에 토큰을 발급*   
<!-- *> - DB에 저장되어있는 게시글의 데이터 리스트를 불러와 전달한다.* <br>
*> - 검색창에 단어를 검색할 경우, 검색어와 검색 상태를 함수 파라미터로 받아와 전달한다.* <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;검색기능을 실행하지 않았을 경우 임의로 status에는 'no', keyword에는 ''빈 값을 전달한다. -->
<!-- 
```python
@app.route('/login/home')
def home_():
    token_receive = request.cookies.get('mytoken')
    payload = jwt.decode(token_receive, SECRET_KEY, algorithms=['HS256'])
    user_id = payload["id"]
    posts = list(db.mountain_info.find({}, {'_id': False}))
    status = request.args.get("searched")
    keyword = request.args.get("keyword")
    
    if status is not None:
        return render_template('index.html', posts=posts, status=status, keyword=keyword, user_id=user_id)
    else:
        return render_template('index.html', posts=posts, status='no', keyword='', user_id=user_id)

```
 -->

### 회원가입 api
<!-- 
*> 로그인페이지 rendering*

```python
@app.route('/login')
def home_login():
    return render_template('register.html', msg=msg)
``` -->

*> 회원가입시 ID pw 확인*

```java
    public Boolean login(LoginRequestDto loginRequestDto){
        Member member = memberRepository.findByUsername(loginRequestDto.getUsername())
                .orElse(null);
        if (member != null) {
            if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
```

<!-- *> 로그인시 아이디 확인*

```python
@app.route('/sign_in', methods=['POST'])
def sign_in():
    
         return jsonify({'result': 'success', 'token': token})
    # 찾지 못하면
    else:
        return jsonify({'result': 'fail', 'msg': '아이디/비밀번호가 일치하지 않습니다.'})
``` -->

*> 회원가입시 ID 중복확인*

```
// 회원 ID 중복 확인
        Optional<Member> found = memberRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 닉네임이 존재합니다.");
        }
```


*> 회원가입시 ID 비밀번호 확인 *

```
// 회원 ID 중복 확인
        if(!password.equals(passwordCk)){
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 같지않습니다 확인해주세요");
        }
```



### 등록 페이지
<!-- 
*> 등록페이지 rendering*

```python
@app.route('/register')
def register_page():
    return render_template("write.html")
``` -->

*> 등록페이지에 들어오는 상세데이터 DB에 저장*

```java
    @PostMapping("/api/notice/write")
    public NoticeResponseDto noticeWrite(@RequestBody @Valid NoticeCreateDto noticeCreateDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return noticeService.noticeWrite(noticeCreateDto, userDetails.getUsername());
       /* ApiResponseMessage message = new ApiResponseMessage("Success", "게시글이 작성 되었습니다.", "", "");
       * return new  ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
       * */
    }
    // service 
        @Transactional
    public NoticeResponseDto noticeWrite(NoticeCreateDto noticeCreateDto, String username){
        Notice notice = Notice.createNotice(noticeCreateDto, username);
        noticeRepository.save(notice);
        NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice);
        return noticeResponseDto;
    }
    // Entity
        public static Notice createNotice(NoticeCreateDto noticeCreateDto, String username){
        Notice notice = new Notice();
        notice.setTitle(noticeCreateDto.getTitle());
        notice.setDescription(noticeCreateDto.getDescription());
        notice.setNoticeDate(noticeCreateDto.getDay());
        notice.setUsername(username);
        notice.setImage(noticeCreateDto.getImage());
        return notice;
    }
```

*> 등록페이지에 들어왔던 데이터 수정 *


```java
    //게시글 수정
    @PatchMapping("/api/notice/change/{id}")
    public NoticeResponseDto changeContents(@PathVariable("id") Long noticeId, @RequestBody NoticeCreateDto noticeCreateDto){
        return noticeService.changeNotice(noticeId, noticeCreateDto);
    }

    // service 
    //게시글 수정
    @Transactional
    public NoticeResponseDto changeNotice(Long noticeId, NoticeCreateDto noticeCreateDto) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        notice.setTitle(noticeCreateDto.getTitle());
        notice.setDescription(noticeCreateDto.getDescription());
        notice.setImage(noticeCreateDto.getImage());
        notice.setNoticeDate(noticeCreateDto.getDay());

        NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice);
        noticeRepository.save(notice);
        return noticeResponseDto;
    }
    }
```

*> 등록페이지에 들어온 데이터 삭제 *
```java
    // 게시글 삭제
    @Transactional
    public void deleteContent(Long contentId, String userName) {
        Notice writer = noticeRepository.findById(contentId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if (Objects.equals(writer.getUsername(), userName)) {
            noticeRepository.delete(writer);
        }else{
            throw new IllegalArgumentException("작성한 유저가 아닙니다.");
        }
    }
```
*> 메인페이지에 데이터 전체 조회 *
```java
    // 게시글 조회
    @GetMapping("/api/notice")
    public List<NoticeResponseDto> getContents() {
        return noticeService.getNotice();
    }
    //service
    // 게시글 조회
    public List<NoticeResponseDto> getNotice() {
        List<Notice> notice = noticeRepository.findAllByOrderByCreatedAtDesc(); //db에서 CreatedAt이라는 값을 기준으로 내림차순 해서 가져와라
        List<NoticeResponseDto> result = notice.stream() .map(n -> new NoticeResponseDto(n)) .collect(Collectors.toList());
        return result;
    }
```

### 게시글 좋아요 기능 

```java
    @PostMapping("/api/loves/{noticeId}")
    public ResponseEntity<Boolean> loveClick(@PathVariable Long noticeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long memberId = userDetails.getMember().getId();
//        loveService.loveUp(noticeId , memberId);
        return new ResponseEntity<>(loveService.loveUp(noticeId , memberId), HttpStatus.OK);
    }

    // service 
public boolean loveUp(Long noticeId, Long memberId) {
        Notice notice = getNotice(noticeId);
        Member member = getMember(memberId);

        //서버에 반환해줄 불리언
        boolean toggleLike;

        LoveDto loveDto = new LoveDto(notice, member);
        Loves loves = new Loves(loveDto);
        int loveCnt = loves.getNotice().getLoveCnt();

        //지금 로그인 되어있는 사용자가 해당 포스트에 좋아요를 누른적이 있냐 없냐.
        Loves findHeart = loveRepository.findByNoticeAndMember(notice, member).orElse(null);

        if(findHeart == null){
            loves.getNotice().setLoveCnt(loveCnt+1);

            loveRepository.save(loves);
            toggleLike = true;
        }
        else{
            loves.getNotice().setLoveCnt(loveCnt-1);

            loveRepository.deleteById(findHeart.getId());
            toggleLike = false;
        }
        return toggleLike;
    }

    private Notice getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                ()->new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return notice;
    }

    private Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
        );
        return member;
    }

```


## 진행방식
> 

<br>

## 진행상황


* 로그인 api
    - 로그인: POST/api/user/login
         - id
         - password
<!--     - 로그인 뷰: GET/login -->
    - 로그아웃: POST/api/user/logout
* 회원가입 api
<!--     - 회원가입 뷰: GET/register -->
    - 회원가입: POST/api/user/signup
         - username
         - password
         - passwordCk
         - nickname
* 메인페이지 api
<!--     - 메인페이지 뷰: GET/main -->
        * List
            - title
            - description
            - image
            - username(id)
            - day
* 게시글 작성 api
<!--     - 게시글 작성 뷰: GET/main/write -->
    - 게시글 작성: POST/main/write
        - Title
        - description
        - image
        - day
        - username(id) -->
 -->
