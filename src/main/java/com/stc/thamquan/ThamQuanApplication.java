package com.stc.thamquan;

import com.stc.thamquan.dtos.EmailDto;
import com.stc.thamquan.dtos.kafka.TextEmail;
import com.stc.thamquan.entities.*;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
import com.stc.thamquan.feigns.ScheduleServiceClient;
import com.stc.thamquan.repositories.*;
import com.stc.thamquan.services.email.EmailService;
import com.stc.thamquan.utils.EnumRole;
import com.stc.thamquan.utils.EnumTrangThai;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableFeignClients
@EnableEurekaClient
@EnableAsync
@SpringBootApplication
public class ThamQuanApplication implements CommandLineRunner {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private DoanhNghiepRepository doanhNghiepRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private DotThamQuanRepository dotThamQuanRepository;

    @Autowired
    private ChuyenThamQuanRepository chuyenThamQuanRepository;

    @Qualifier("com.stc.thamquan.feigns.ScheduleServiceClient")
    @Autowired
    private ScheduleServiceClient scheduleServiceClient;

    @Autowired
    private EmailService emailService;

    @Value("${spring.application.name}")
    private String cliendId;

    @Autowired
    private CongTacVienRepository congTacVienRepository;

    public static void main(String[] args) {
        SpringApplication.run(ThamQuanApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        TextEmail textEmail = new TextEmail();
//        textEmail.setClientId(cliendId);
//        textEmail.setEmailSender("dev5.stc@hcmute.edu.vn");
//        textEmail.setPassWordSender("stc-dev@123456");
//        textEmail.setSendTos(Collections.singletonList("thangpx@hcmute.edu.vn"));
//        textEmail.setSubject("Email Job From Tham Quan");
//        textEmail.setMessage("Schedule Job Testing From Tham Quan");
//        EmailDto dto = new EmailDto(new LocalDateTime().plusMinutes(1).toDate(),
//                "Send email with job schedule from tham quan", textEmail, null);
//        scheduleServiceClient.createEmailSchedule(dto);

//        File file = new ClassPathResource("/models/template-import-doanh-nghiep.xlsx").getFile();
//        emailService.sendFileMail(
//                "thangpx@hcmute.edu.vn",
//                "Test file mail",
//                null,
//                null,
//                "File mail",
//                Collections.singletonList(file)
//        );
        initData();

        if (congTacVienRepository.count() == 0) {
            CongTacVien congTacVien = new CongTacVien();
            congTacVien.setHoTen("Cộng tác viên");
            congTacVien.setEmail("daotaosaudaihocspkt@gmail.com");
            congTacVien.setPassword("123456@$2021");
            congTacVien.setRoles(Collections.singletonList(EnumRole.ROLE_CONG_TAC_VIEN.name()));
            congTacVienRepository.save(congTacVien);
        }
    }

    private void initData() {
        if (taiKhoanRepository.count() == 0) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setEmail("thangpx@hcmute.edu.vn");
            taiKhoan.setPassword("123456@$2021");
            taiKhoan.setHoTen("Thắng Phạm");
            taiKhoan.setTrangThai(true);
            taiKhoan.setRoles(Arrays.asList(EnumRole.ROLE_ADMIN.name(), EnumRole.ROLE_CONG_TAC_VIEN.name()));
            taiKhoanRepository.save(taiKhoan);
            System.out.println("Complete init tài khoản");
        }

        if (giangVienRepository.count() == 0) {
            GiangVien giangVien = new GiangVien();
            giangVien.setEmail("phamthangspkt@gmail.com");
            giangVien.setHoTen("Thắng Phạm");
            giangVien.setPassword("123456@$2021");
            giangVien.setThinhGiang(false);
            giangVien.setTrangThai(true);
            giangVien.setRoles(Collections.singletonList(EnumRole.ROLE_GIANG_VIEN.name()));
            giangVienRepository.save(giangVien);
            System.out.println("Complete init giảng viên");
        }

        if (sinhVienRepository.count() == 0) {
            SinhVien sinhVien = new SinhVien();
            sinhVien.setEmail("15110008@student.hcmute.edu.vn");
            sinhVien.setHoTen("Nguyễn Quốc Anh");
            sinhVien.setRoles(Collections.singletonList(EnumRole.ROLE_SINH_VIEN.name()));
            sinhVien.setTrangThai(true);
            sinhVien.setPassword("123456@$2021");
            sinhVienRepository.save(sinhVien);
            System.out.println("Complete init doanh nghiệp");
        }

        if (doanhNghiepRepository.count() == 0) {
            DoanhNghiep caNhan = new DoanhNghiep();
            caNhan.setEmail("thangphamspk@gmail.com");
            caNhan.setHoTen("Thắng Phạm");
            caNhan.setRoles(Collections.singletonList(EnumRole.ROLE_DOANH_NGHIEP.name()));
            caNhan.setPassword("123456@$2021");
            caNhan.setTrangThai(true);
            doanhNghiepRepository.save(caNhan);
            System.out.println("Complete init sinh viên");
        }
        if (dotThamQuanRepository.count() == 0) {
            DotThamQuan dotThamQuan = new DotThamQuan();
            dotThamQuan.setTenDotThamQuan("Đợt thăm quan tháng 4/2021");
            dotThamQuan.setTuNgay(LocalDate.now().minusDays(10).toDate());
            dotThamQuan.setDenNgay(LocalDate.now().plusDays(10).toDate());
            dotThamQuan.setTrangThai(true);
            dotThamQuanRepository.save(dotThamQuan);
        }
        if (chuyenThamQuanRepository.count() == 0) {
            DotThamQuan dotThamQuan = dotThamQuanRepository.findAll().get(0);
            DoanhNghiep doanhNghiep = doanhNghiepRepository.findAll().get(0);
            ChuyenThamQuan chuyenThamQuan = new ChuyenThamQuan();
            chuyenThamQuan.setDotThamQuan(dotThamQuan);
            chuyenThamQuan.setDoanhNghiep(doanhNghiep);
//            chuyenThamQuan.setCongTacViens(taiKhoanRepository.findAll());
            List<SinhVienThamQuan> sinhVienThamQuans = new ArrayList<>();
            sinhVienRepository.findAll().forEach(sinhVien -> {
                SinhVienThamQuan sinhVienThamQuan = new SinhVienThamQuan();
                sinhVienThamQuan.setSinhVien(sinhVien);
                sinhVienThamQuan.setTrangThai(true);
                sinhVienThamQuans.add(sinhVienThamQuan);
            });
            chuyenThamQuan.setDanhSachSinhViens(sinhVienThamQuans);
            chuyenThamQuan.setPhuCapCongTacVien(50000);
            chuyenThamQuan.setTrangThai(EnumTrangThai.DA_LIEN_HE_CONG_TY.name());
            chuyenThamQuanRepository.save(chuyenThamQuan);

        }
    }
}
