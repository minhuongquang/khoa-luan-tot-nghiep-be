package com.stc.thamquan.dtos.kafka;

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
 * Time      : 13:36
 * Filename  : TextEmail
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TextEmail {
    private String clientId;

    private String emailSender;

    private String passWordSender;

    private List<String> sendTos = new ArrayList<>();

    private String subject;

    private String message;

    public TextEmail(List<String> sendTos, String subject, String message) {
        this.sendTos = sendTos;
        this.subject = subject;
        this.message = message;
    }
}
