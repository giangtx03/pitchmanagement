package com.pitchmanagement.constants;

public class MailConstant {
    public static final String FROM_EMAIL = "pitch.manager.firms@gmail.com";
    public static final String REGISTRATION_CONFIRMATION =
            "<!DOCTYPE html>" +
            "<html lang=\"en\">" +
            "<head>" +
            "<meta charset=\"UTF-8\">" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "<title>Confirm Your Account</title>" +
            "</head>" +
            "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; margin: 0; padding: 20px; text-align: center;\">" +
            "<div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
            "<div style=\"padding-bottom: 20px;\">" +
            "<img src=\"https://example.com/logo.png\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
            "<h1 style=\"color: #4CAF50; font-size: 24px;\">Confirm Your Account</h1>" +
            "</div>" +
            "<p style=\"font-size: 16px; line-height: 1.6; color: #555;\">Hello {username},</p>" +
            "<p style=\"font-size: 16px; line-height: 1.6; color: #555;\">Thank you for signing up with us! To complete your registration, please confirm your email address by clicking the button below:</p>" +
            "<a href=\"{confirmationLink}\" style=\"display: inline-block; padding: 12px 25px; background-color: #4CAF50; color: #ffffff; text-decoration: none; border-radius: 5px; font-size: 16px; margin-top: 20px;\">Confirm Account</a>" +
            "<p style=\"font-size: 16px; line-height: 1.6; color: #555; margin-top: 20px;\">If you did not create an account, you can safely ignore this email.</p>" +
            "<div style=\"margin-top: 30px; font-size: 12px; color: #999;\">" +
            "<p style=\"margin: 5px 0;\">Need help? Contact us at <a href=\"mailto:support@example.com\" style=\"color: #4CAF50; text-decoration: none;\">support@example.com</a>.</p>" +
            "<p style=\"margin: 5px 0;\">&copy; 2024 Your Company. All rights reserved.</p>" +
            "</div>" +
            "</div>" +
            "</body>" +
            "</html>";

    public static final String FORGOT_PASSWORD =
            "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<title>Confirm Your Account</title>" +
                    "</head>" +
                    "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; margin: 0; padding: 20px; text-align: center;\">" +
                    "<div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                    "<div style=\"padding-bottom: 20px;\">" +
                    "<img src=\"https://example.com/logo.png\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
                    "<h1 style=\"color: #4CAF50; font-size: 24px;\">Confirm Your Account</h1>" +
                    "</div>" +
                    "<p style=\"font-size: 16px; line-height: 1.6; color: #555;\">Hello {username},</p>" +
                    "<p style=\"font-size: 16px; line-height: 1.6; color: #555;\">Cảm ơn bạn đã sử dụng website của chúng thôi! Để đặt lại mật khẩu của bạn vui lòng bấm vào nút dưới đây:</p>" +
                    "<a href=\"{confirmationLink}\" style=\"display: inline-block; padding: 12px 25px; background-color: #4CAF50; color: #ffffff; text-decoration: none; border-radius: 5px; font-size: 16px; margin-top: 20px;\">Confirm Account</a>" +
                    "<div style=\"margin-top: 30px; font-size: 12px; color: #999;\">" +
                    "<p style=\"margin: 5px 0;\">Need help? Contact us at <a href=\"mailto:support@example.com\" style=\"color: #4CAF50; text-decoration: none;\">support@example.com</a>.</p>" +
                    "<p style=\"margin: 5px 0;\">&copy; 2024 Your Company. All rights reserved.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

    public static final String SUBJECT_TYPE_CONFIRM_EMAIL = "Xác thực email";
    public static final String SUBJECT_TYPE_FORGOT_PASSWORD = "Đặt lại mật khẩu";
}