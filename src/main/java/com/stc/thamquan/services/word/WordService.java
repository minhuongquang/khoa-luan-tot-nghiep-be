package com.stc.thamquan.services.word;

import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.entities.DotThamQuan;

import java.io.File;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:47
 * Filename  : WordService
 */
public interface WordService {

    File xuatCongVanXinThamQuanGuiDoanhNghiep(ChuyenThamQuan chuyenThamQuan) throws Exception;

    File xuatKeHoachThamQuan(DotThamQuan dotThamQuan, List<ChuyenThamQuan> chuyenThamQuan)throws Exception;

    File xuatPhieuXacNhanThamQuan(ChuyenThamQuan chuyenThamQuan) throws Exception;

}
