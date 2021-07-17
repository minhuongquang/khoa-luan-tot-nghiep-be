package com.stc.thamquan.services.email;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 14:08
 * Filename  : EmailService
 */
public interface EmailService {
    /***
     * @author: thangpx
     * @param nguoiNhanThu: Email người nhận thư
     * @param subject: Chủ đề thư
     * @param templateName: Tên template mail trong folder templates (ví dụ: forget-password.ftl), truyền null hoặc rỗng để sử dụng text
     * @param model: Model cho template mail
     * @param message: Nội dung tin nhắn trong trường hợp không sử dụng template mail
     */
    void sendTextMail(String nguoiNhanThu, String subject, String templateName, Map<String, Object> model, String message);

    /***
     *
     * @param nguoiNhanThu
     * @param subject
     * @param templateName
     * @param model
     * @param message
     * @param files
     */
    void sendFileMail(String nguoiNhanThu, String subject, String templateName, Map<String, Object> model, String message, List<File> files);
}
