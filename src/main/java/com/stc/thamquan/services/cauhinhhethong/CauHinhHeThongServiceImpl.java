package com.stc.thamquan.services.cauhinhhethong;

import com.stc.thamquan.dtos.cauhinhhethong.CauHinhHeThongDto;
import com.stc.thamquan.entities.CauHinhHeThong;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.CauHinhHeThongRepository;
import com.stc.thamquan.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 14:24
 * Filename  : CauHinhHeThongServiceImpl
 */
@Slf4j
@Service
public class CauHinhHeThongServiceImpl implements CauHinhHeThongService {
    private final CauHinhHeThongRepository cauHinhHeThongRepository;

    public CauHinhHeThongServiceImpl(CauHinhHeThongRepository cauHinhHeThongRepository) {
        this.cauHinhHeThongRepository = cauHinhHeThongRepository;
    }

    @Override
    public CauHinhHeThong getCauHinhHeThongCore() {
        List<CauHinhHeThong> cauHinhHeThongs = cauHinhHeThongRepository.findAll();
        if (cauHinhHeThongs.size() == 0) {
            throw new NotFoundException("Cấu hình hệ thống không tồn tại, vui lòng tạo mới");
        }
        return cauHinhHeThongs.get(0);
    }

    @Override
    public Page<CauHinhHeThong> getAllCauHinhHeThongPaging(int page, int size, String sort, String column){
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return cauHinhHeThongRepository.findAll(pageable);
    }

    @Override
    public CauHinhHeThong getCauHinhHeThong(String id){
        return  cauHinhHeThongRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Cấu hình hệ thống có id %s không tồn tại",id)));
    }


    @Override
    public CauHinhHeThong createCauHinhHeThong(CauHinhHeThongDto dto){

        if(ObjectUtils.isEmpty(dto.getEmailGuiThu())){
            throw new InvalidException("Email gửi thư không được bỏ trống");
        }
        if(ObjectUtils.isEmpty(dto.getPasswordEmailGuiThu())){
            throw new InvalidException("Password mail gửi thư không được bỏ trống");
        }

        if(ObjectUtils.isEmpty(dto.getEmailNhanThu())){
            throw new InvalidException("Email nhận thư không được bỏ trống");
        }

        if(cauHinhHeThongRepository.count() != 0){
            throw new InvalidException("Cấu hình hệ thống đã tồn tại");
        }
        CauHinhHeThong cauHinhHeThong = new CauHinhHeThong();
        cauHinhHeThong.setEmailGuiThu(dto.getEmailGuiThu());
        cauHinhHeThong.setPasswordEmailGuiThu(dto.getPasswordEmailGuiThu());
        cauHinhHeThong.setEmailNhanThu(dto.getEmailNhanThu());
        cauHinhHeThong.setPhuCapCongTacVien(dto.getPhuCapCongTacVien());
        cauHinhHeThongRepository.save(cauHinhHeThong);
        return cauHinhHeThong;
    }

    @Override
    public CauHinhHeThong updateCauHinhHeThong(String id, CauHinhHeThongDto dto) {
        CauHinhHeThong cauHinhHeThong = getCauHinhHeThong(id);
        if(ObjectUtils.isEmpty(dto.getEmailGuiThu())){
            throw new InvalidException("Email gửi thư không được bỏ trống");
        }
        if(ObjectUtils.isEmpty(dto.getPasswordEmailGuiThu())){
            throw new InvalidException("Password mail gửi thư không được bỏ trống");
        }
        if(ObjectUtils.isEmpty(dto.getEmailNhanThu())){
            throw new InvalidException("Email nhận thư không được bỏ trống");
        }
        cauHinhHeThong.setEmailGuiThu(dto.getEmailGuiThu());
        cauHinhHeThong.setPasswordEmailGuiThu(dto.getPasswordEmailGuiThu());
        cauHinhHeThong.setEmailNhanThu(dto.getEmailNhanThu());
        cauHinhHeThong.setPhuCapCongTacVien(dto.getPhuCapCongTacVien());
        cauHinhHeThongRepository.save(cauHinhHeThong);
        return cauHinhHeThong;
    }

    @Override
    public CauHinhHeThong updatePhuCapCTV(double phuCap) {
        if(phuCap < 0) {
            throw new InvalidException("Phụ cấp không hợp lệ");
        }

        CauHinhHeThong cauHinhHeThong = getCauHinhHeThongCore();
        cauHinhHeThong.setPhuCapCongTacVien(phuCap);
        cauHinhHeThongRepository.save(cauHinhHeThong);
        return cauHinhHeThong;
    }

    @Override
    public double getPhuCapCTV() {
        CauHinhHeThong cauHinhHeThong = getCauHinhHeThongCore();
        return cauHinhHeThong.getPhuCapCongTacVien();
    }
}
