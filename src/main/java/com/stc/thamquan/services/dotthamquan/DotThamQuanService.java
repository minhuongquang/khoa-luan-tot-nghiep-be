package com.stc.thamquan.services.dotthamquan;

import com.stc.thamquan.dtos.dotthamquan.DotThamQuanDto;
import com.stc.thamquan.entities.DotThamQuan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:59
 * Filename  : DotThamQuanService
 */
public interface DotThamQuanService {
    Page<DotThamQuan> getAllDotThamQuansPaging(String search, int page, int size, String sort, String column);

    Page<DotThamQuan> getAllDotThamQuanActive(String search, int page, int size, String sort, String column);

    DotThamQuan getDotThamQuan(String id);

    DotThamQuan getDotThamQuanByIdCore(String id);

    DotThamQuan createDotThamQuan(DotThamQuanDto dto);

    DotThamQuan updateDotThamQuan(String id, DotThamQuanDto dto);

    DotThamQuan changeStatus(String id);

    List<DotThamQuan> getAllByTrangThaiTrue();

}
