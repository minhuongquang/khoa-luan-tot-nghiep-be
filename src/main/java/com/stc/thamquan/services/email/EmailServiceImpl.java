package com.stc.thamquan.services.email;

import com.stc.thamquan.dtos.kafka.FileEmail;
import com.stc.thamquan.dtos.kafka.TextEmail;
import com.stc.thamquan.entities.CauHinhHeThong;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.services.cauhinhhethong.CauHinhHeThongService;
import com.stc.thamquan.services.kafka.EmailKafkaService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 14:08
 * Filename  : EmailServiceImpl
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private final CauHinhHeThongService cauHinhHeThongService;

    private final EmailKafkaService emailKafkaService;

    private final Configuration freemarkerConfig;

    private final MessageSource messageSource;

    @Value("${spring.application.name}")
    private String cliendId;

    public EmailServiceImpl(CauHinhHeThongService cauHinhHeThongService,
                            EmailKafkaService emailKafkaService, Configuration freemarkerConfig, MessageSource messageSource) {
        this.cauHinhHeThongService = cauHinhHeThongService;
        this.emailKafkaService = emailKafkaService;
        this.freemarkerConfig = freemarkerConfig;
        this.messageSource = messageSource;
    }


    /***
     * @author: thangpx
     * @createdDate: 09/04/2021
     * @param nguoiNhanThu: Email người nhận thư
     * @param subject: Chủ đề thư
     * @param templateName: Tên file template trong thư mục templates, ví dụ: forget-password.ftl
     * @param model: Model truyền vào cho template
     * @param message: nội dung thư trong trường hợp không sử dụng template mail
     */
    @Override
    public void sendTextMail(String nguoiNhanThu, String subject, String templateName, Map<String, Object> model, String message) {
        Locale locale = LocaleContextHolder.getLocale();
        CauHinhHeThong cauHinhHeThong = cauHinhHeThongService.getCauHinhHeThongCore();
        TextEmail textEmail = new TextEmail();
        textEmail.setClientId(cliendId);
        textEmail.setEmailSender(cauHinhHeThong.getEmailGuiThu());
        textEmail.setPassWordSender(cauHinhHeThong.getPasswordEmailGuiThu());
        textEmail.setSendTos(Collections.singletonList(nguoiNhanThu));
        // nếu không truyền tên template thì gửi mail text
        if (ObjectUtils.isEmpty(templateName)) {
            if (ObjectUtils.isEmpty(message)) {
                throw new InvalidException(messageSource.getMessage("error.messageempty", null, locale));
            }
            textEmail.setSubject(subject);
            textEmail.setMessage(message);
            emailKafkaService.sendTextEmail(textEmail);
        } else {
            try {
                Template template = freemarkerConfig.getTemplate(templateName);
                freemarkerConfig.setDefaultEncoding("UTF-8");
                freemarkerConfig.setLocale(Locale.US);
                String messageMail = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                textEmail.setSubject(subject);
                textEmail.setMessage(messageMail);
                emailKafkaService.sendTemplateEmail(textEmail);
            } catch (Exception e) {
                throw new InvalidException(String.format("Error: %s", e.getMessage()));
            }
        }

    }

    @Override
    public void sendFileMail(String nguoiNhanThu, String subject, String templateName, Map<String, Object> model, String message, List<File> files) {
        Locale locale = LocaleContextHolder.getLocale();
        CauHinhHeThong cauHinhHeThong = cauHinhHeThongService.getCauHinhHeThongCore();
        if (ObjectUtils.isEmpty(templateName)) {
            if (ObjectUtils.isEmpty(message)) {
                throw new InvalidException(messageSource.getMessage("error.messageempty", null, locale));
            }
            FileEmail fileEmail = new FileEmail(
                    cliendId,
                    cauHinhHeThong.getEmailGuiThu(),
                    cauHinhHeThong.getPasswordEmailGuiThu(),
                    Collections.singletonList(nguoiNhanThu),
                    subject,
                    message,
                    files
            );
            emailKafkaService.sendFileEmail(fileEmail);
        } else {
            try {
                Template template = freemarkerConfig.getTemplate(templateName);
                freemarkerConfig.setDefaultEncoding("UTF-8");
                freemarkerConfig.setLocale(Locale.US);
                String messageMail = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                FileEmail fileEmail = new FileEmail(
                        cliendId,
                        cauHinhHeThong.getEmailGuiThu(),
                        cauHinhHeThong.getPasswordEmailGuiThu(),
                        Collections.singletonList(nguoiNhanThu),
                        subject,
                        messageMail,
                        files
                );
                emailKafkaService.sendFileEmail(fileEmail);
            } catch (Exception e) {
                throw new InvalidException(String.format("Error: %s", e.getMessage()));
            }
        }
    }
}
