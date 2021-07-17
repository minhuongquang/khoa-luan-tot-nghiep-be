package com.stc.thamquan.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 13:34
 * Filename  : TokenDetails
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {

    private String fullName;

    private String token;

    private String avatar;

    private long expired;

    private List<String> roles = new ArrayList<>();
}

