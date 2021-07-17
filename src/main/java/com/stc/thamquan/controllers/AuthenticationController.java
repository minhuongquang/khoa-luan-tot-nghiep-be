package com.stc.thamquan.controllers;

import com.stc.thamquan.dtos.AccountDto;
import com.stc.thamquan.dtos.TokenDetails;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.UserNotFoundAuthenticationException;
import com.stc.thamquan.securities.*;
import com.stc.thamquan.securities.provider.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 19:06
 * Filename  : AuthenticationController
 */
@RestController
@RequestMapping("/rest/login")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TaiKhoanDetailsService taiKhoanDetailsService;

    @Autowired
    private GiangVienDetailsService giangVienDetailsService;

    @Autowired
    private SinhVienDetailsService sinhVienDetailsService;

    @Autowired
    private DoanhNghiepDetailsService doanhNghiepDetailsService;

    @Autowired
    private CongTacVienDetailsService congTacVienDetailsService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${google.verifyUrl}")
    private String googleVerifyUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    //<editor-fold desc="Admin Login">

    /***
     * @author: thangpx
     * @createDate: 31-03-2021
     * @param dto: DTO login form (username, password)
     * @return: Token login
     */
    @ApiOperation(value = "Admin, cộng tác viên login form (username, password), avatar null")
    @PostMapping("/admin")
    public ResponseEntity<TokenDetails> loginAdmin(@Valid @RequestBody AccountDto dto) {
        TaiKhoanAuthenticationToken authenticationToken = new TaiKhoanAuthenticationToken(
                dto.getUsername(),
                dto.getPassword(),
                true
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (UserNotFoundAuthenticationException | BadCredentialsException ex) {
            throw new InvalidException(ex.getMessage());
        }
        final JwtUserDetails userDetails = taiKhoanDetailsService
                .loadUserByUsername(dto.getUsername());
        final TokenDetails result = jwtTokenUtils.getTokenDetails(userDetails, null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 31-03-2021
     * @lastModified:
     * @changeBy:
     * @lastChange:
     * @param googleToken: Token Id google
     * @return: Token login
     */
    @ApiOperation(value = "Admin, cộng tác viên login google (Token Id), lấy avatar google")
    @PostMapping("/admin/google")
    public ResponseEntity<TokenDetails> loginGoogleAdmin(@RequestHeader(name = "idToken") String googleToken) {
        Locale locale = LocaleContextHolder.getLocale();
        String urlRequest = googleVerifyUrl + googleToken;
        String email;
        String avatar;
        try {
            ResponseEntity<HashMap> responseEntity = restTemplate.exchange(urlRequest, HttpMethod.GET, null, HashMap.class);
            HashMap<String, String> map = responseEntity.getBody();
            email = map.get("email");
            avatar = map.get("picture");
        } catch (Exception ex) {
            throw new InvalidException("Token không hợp lệ");
        }
        TaiKhoanAuthenticationToken authenticationToken = new TaiKhoanAuthenticationToken(
                email,
                null,
                false
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (UserNotFoundAuthenticationException | BadCredentialsException ex) {
            throw new InvalidException(ex.getMessage());
        }
        final JwtUserDetails userDetails = taiKhoanDetailsService
                .loadUserByUsername(email);
        final TokenDetails result = jwtTokenUtils.getTokenDetails(userDetails, avatar);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //</editor-fold>

    //<editor-fold desc="Sinh viên Login">
    /***
     * @author: thangpx
     * @createDate: 31-03-2021
     * @param googleToken: Token Id google
     * @return: Token login
     */
    @ApiOperation(value = "Sinh viên login google (Token Id), lấy avatar google, nếu chưa có tài khoản thì tạo tài khoản cho sinh viên")
    @PostMapping("/sinh-vien/google")
    public ResponseEntity<TokenDetails> loginGoogleSinhVien(@RequestHeader(name = "idToken") String googleToken) {
        Locale locale = LocaleContextHolder.getLocale();
        String urlRequest = googleVerifyUrl + googleToken;
        String email;
        String avatar;
        String name;
        try {
            ResponseEntity<HashMap> responseEntity = restTemplate.exchange(urlRequest, HttpMethod.GET, null, HashMap.class);
            HashMap<String, String> map = responseEntity.getBody();
            email = map.get("email");
            avatar = map.get("picture");
            name = map.get("name");
        } catch (Exception ex) {
            throw new InvalidException("Token không hợp lệ");
        }
        SinhVienAuthenticationToken authenticationToken = new SinhVienAuthenticationToken(
                email,
                null,
                false
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (UserNotFoundAuthenticationException | BadCredentialsException ex) {
            throw new InvalidException(ex.getMessage());
        }
        final JwtUserDetails userDetails = sinhVienDetailsService
                .loadUserByUsername(email);
        final TokenDetails result = jwtTokenUtils.getTokenDetails(userDetails, avatar);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //</editor-fold>

    //<editor-fold desc="Giảng viên login">
    /***
     * @author: thangpx
     * @createDate: 31-03-2021
     * @param googleToken: Token Id google
     * @return: Token login
     */
    @ApiOperation(value = "Giảng viên login google (Token Id), lấy avatar google")
    @PostMapping("/giang-vien/google")
    public ResponseEntity<TokenDetails> loginGoogleGiangVien(@RequestHeader(name = "idToken") String googleToken) {
        Locale locale = LocaleContextHolder.getLocale();
        String urlRequest = googleVerifyUrl + googleToken;
        String email;
        String avatar;
        try {
            ResponseEntity<HashMap> responseEntity = restTemplate.exchange(urlRequest, HttpMethod.GET, null, HashMap.class);
            HashMap<String, String> map = responseEntity.getBody();
            email = map.get("email");
            avatar = map.get("picture");
        } catch (Exception ex) {
            throw new InvalidException("Token không hợp lệ");
        }
        GiangVienAuthenticationToken authenticationToken = new GiangVienAuthenticationToken(
                email,
                null,
                false
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (UserNotFoundAuthenticationException | BadCredentialsException ex) {
            throw new InvalidException(ex.getMessage());
        }
        final JwtUserDetails userDetails = giangVienDetailsService
                .loadUserByUsername(email);
        final TokenDetails result = jwtTokenUtils.getTokenDetails(userDetails, avatar);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //</editor-fold>

    //<editor-fold desc="Cá nhân ngoài trường login">

    /***
     * @author: thangpx
     * @createDate: 31-03-2021
     * @param dto: DTO login form (username, password)
     * @return: Token login
     */
    @ApiOperation(value = "Doanh nghiệp login form (username, password), avatar null")
    @PostMapping("/doanh-nghiep")
    public ResponseEntity<TokenDetails> loginCaNhan(@Valid @RequestBody AccountDto dto) {
        DoanhNghiepAuthenticationToken authenticationToken = new DoanhNghiepAuthenticationToken(
                dto.getUsername(),
                dto.getPassword(),
                true
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (UserNotFoundAuthenticationException | BadCredentialsException ex) {
            throw new InvalidException(ex.getMessage());
        }
        final JwtUserDetails userDetails = doanhNghiepDetailsService
                .loadUserByUsername(dto.getUsername());
        final TokenDetails result = jwtTokenUtils.getTokenDetails(userDetails, null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 31-03-2021
     * @param googleToken: Token Id google
     * @return: Token login
     */
    @ApiOperation(value = "Doanh nghiệp login google (Token Id), lấy avatar google")
    @PostMapping("/doanh-nghiep/google")
    public ResponseEntity<TokenDetails> loginGoogleCaNhan(@RequestHeader(name = "idToken") String googleToken) {
        Locale locale = LocaleContextHolder.getLocale();
        String urlRequest = googleVerifyUrl + googleToken;
        String email;
        String avatar;
        try {
            ResponseEntity<HashMap> responseEntity = restTemplate.exchange(urlRequest, HttpMethod.GET, null, HashMap.class);
            HashMap<String, String> map = responseEntity.getBody();
            email = map.get("email");
            avatar = map.get("picture");
        } catch (Exception ex) {
            throw new InvalidException("Token không hợp lệ");
        }
        DoanhNghiepAuthenticationToken authenticationToken = new DoanhNghiepAuthenticationToken(
                email,
                null,
                false
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (UserNotFoundAuthenticationException | BadCredentialsException ex) {
            throw new InvalidException(ex.getMessage());
        }
        final JwtUserDetails userDetails = doanhNghiepDetailsService
                .loadUserByUsername(email);
        final TokenDetails result = jwtTokenUtils.getTokenDetails(userDetails, avatar);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //</editor-fold>


    //<editor-fold desc="Cá nhân ngoài trường login">

    /***
     * @author: thangpx
     * @createDate: 31-03-2021
     * @param dto: DTO login form (username, password)
     * @return: Token login
     */
    @ApiOperation(value = "Cộng tác viên login form (username, password), avatar null")
    @PostMapping("/cong-tac-vien")
    public ResponseEntity<TokenDetails> loginCongTacVien(@Valid @RequestBody AccountDto dto) {
        CongTacVienAuthenticationToken authenticationToken = new CongTacVienAuthenticationToken(
                dto.getUsername(),
                dto.getPassword(),
                true
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (UserNotFoundAuthenticationException | BadCredentialsException ex) {
            throw new InvalidException(ex.getMessage());
        }
        final JwtUserDetails userDetails = congTacVienDetailsService
                .loadUserByUsername(dto.getUsername());
        final TokenDetails result = jwtTokenUtils.getTokenDetails(userDetails, null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 31-03-2021
     * @param googleToken: Token Id google
     * @return: Token login
     */
    @ApiOperation(value = "Cộng tác viên login google (Token Id), lấy avatar google")
    @PostMapping("/cong-tac-vien/google")
    public ResponseEntity<TokenDetails> loginGoogleCongTacVien(@RequestHeader(name = "idToken") String googleToken) {
        String urlRequest = googleVerifyUrl + googleToken;
        String email;
        String avatar;
        try {
            ResponseEntity<HashMap> responseEntity = restTemplate.exchange(urlRequest, HttpMethod.GET, null, HashMap.class);
            HashMap<String, String> map = responseEntity.getBody();
            email = map.get("email");
            avatar = map.get("picture");
        } catch (Exception ex) {
            throw new InvalidException("Token không hợp lệ");
        }
        CongTacVienAuthenticationToken authenticationToken = new CongTacVienAuthenticationToken(
                email,
                null,
                false
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (UserNotFoundAuthenticationException | BadCredentialsException ex) {
            throw new InvalidException(ex.getMessage());
        }
        final JwtUserDetails userDetails = congTacVienDetailsService
                .loadUserByUsername(email);
        final TokenDetails result = jwtTokenUtils.getTokenDetails(userDetails, avatar);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //</editor-fold>

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('CONG_TAC_VIEN')")
    public ResponseEntity<String> sayHello(Principal principal) {
        return new ResponseEntity<>(String.format("Hello %s", principal.getName()), HttpStatus.OK);
    }

}
