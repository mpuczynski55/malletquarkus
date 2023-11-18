package com.agh.mallet.domain.user.user.control.utils;

public class EmailTemplateProvider {

    public static String getEmailConfirmationTemplate(String confirmationLink) {
        return "<html xmlns:th=\"https://www.thymeleaf.org/\" lang=\"pl\">\n" +
                "<head>\n" +
                "    <style>\n" +
                "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;500;700&display=swap');\n" +
                "\n" +
                "        body {\n" +
                "            background-color: #83CEE5;\n" +
                "            font-family: 'Fredoka One', sans-serif;\n" +
                "        }\n" +
                "\n" +
                "        .blue-background {\n" +
                "            background-color: #00b5d8;\n" +
                "            width: 100%;\n" +
                "            height: 200px;\n" +
                "        }\n" +
                "\n" +
                "        .mail-body {\n" +
                "            border-radius: 20px;\n" +
                "            background-color: #FFFFFF;\n" +
                "            max-width: 600px;\n" +
                "            line-height: 20px;\n" +
                "            margin: -200px auto;\n" +
                "            margin-bottom: 50px;\n" +
                "        }\n" +
                "\n" +
                "        .mail-body .welcome {\n" +
                "            font-weight: 500;\n" +
                "            font-size: 50px;\n" +
                "            text-align: center;\n" +
                "            font-family: \"Fredoka One\", sans-serif;\n" +
                "            margin-top: 110px;\n" +
                "            padding-top: 50px;\n" +
                "            margin-bottom: 60px;\n" +
                "        }\n" +
                "\n" +
                "        p,\n" +
                "        .link {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            margin-bottom: 40px;\n" +
                "            padding: 0 30px;\n" +
                "            font-weight: 300;\n" +
                "        }\n" +
                "\n" +
                "        .cta-button {\n" +
                "            background-color: #00b5d8;\n" +
                "            color: white;\n" +
                "            border-radius: 20px;\n" +
                "            text-decoration: none;\n" +
                "            display: block;\n" +
                "            margin-left: 25%;\n" +
                "            margin-right: 25%;\n" +
                "            padding: 15px;\n" +
                "            text-align: center;\n" +
                "            font-family: \"Open Sans Bold\", sans-serif;\n" +
                "            margin-bottom: 40px;\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        .link {\n" +
                "            display: block;\n" +
                "            color: #00b5d8;\n" +
                "            font-weight: 500;\n" +
                "        }\n" +
                "\n" +
                "        .signature {\n" +
                "            padding-bottom: 5px;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        .aligncenter {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"main\">\n" +
                "    <div class=\"blue-background\"></div>\n" +
                "\n" +
                "    <div class=\"mail-body\">\n" +
                "        <p class=\"welcome\">Welcome to Sanguage!</p>\n" +
                "        <p class=\"aligncenter\">\n" +
                "            <a href=\"https://imgbb.com/\"><img src=\"https://i.ibb.co/qrs04J9/mascot.png\" alt=\"mascot\" width=\"100\" height=\"100\" style=\"align:center\"></a>\n" +
                "        </p>\n" +
                "        <p>Click below to confirm your account:</p>\n" +
                "        <a class=\"cta-button\" href=\"#\" th:href=\"@{__${link}__}\">Confirm Account </a>\n" +
                "        <p>If that doesn't work, copy and paste the following link in your browser:</p>\n" +
                "        <a class=\"link\" href=\"#\" th:href=\"@{__${link}__}\" th:text=\"${link}\">\n" +
                "        </a>\n" +
                "        <p class=\"signature\"></p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getEmailConfirmedTemplate() {
        return "<div style=\"margin: 0; padding: 0; font-family:'sans-serif';color:#6e6e6e;\">\n" +
                "  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "    <tr>\n" +
                "      <td>\n" +
                "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\">\n" +
                "          <tr>\n" +
                "            <td align=\"center\" bgcolor=\"#00b5d8\" style=\"padding: 30px 0 0px 30px;color: #ffffff;font-weight: 700;font-size:50px; font-family:sans-serif;\">\n" +
                "              Sanguage\n" +
                "            </td>\n" +
                "            <tr>\n" +
                "            <td align=\"center\" bgcolor=\"#00b5d8\" style=\"padding: 0 0 30px 30px;color: #ffffff;font-weight: 700;font-size:13px; font-family:sans-serif;\">\n" +
                "              WITH ALL THE BASIC INGREDIENTS\n" +
                "              </td></tr>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td bgcolor=\"#83CEE5\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                "              <!-- content area -->\n" +
                "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                <tr>\n" +
                "                  <td align=\"center\">\n" +
                "                    <a href=\"https://imgbb.com/\"><img src=\"https://i.ibb.co/qrs04J9/mascot.png\" width=150 height=150 alt=\"mascot\" border=\"0\"></a>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                  <td style=\"padding: 50px 0 0px 0px;color: #ffffff;font-weight: 700;font-size:24px;font-family:sans-serif;\">\n" +
                "                    Thank you for joining Sanguage! You can now start learning!\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              \n" +
                "\n" +
                "                </body>";
    }
}
