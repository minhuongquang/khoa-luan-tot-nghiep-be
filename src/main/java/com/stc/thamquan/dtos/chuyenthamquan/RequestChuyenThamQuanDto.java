package com.stc.thamquan.dtos.chuyenthamquan;

import com.stc.thamquan.dtos.dotthamquan.DotThamQuanDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChuyenThamQuanDto {
    private DotThamQuanDto dotThamQuan;

    private List<String> chuyenThamQuans = new ArrayList<>();
}
