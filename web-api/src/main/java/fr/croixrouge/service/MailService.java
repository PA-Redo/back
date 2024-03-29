package fr.croixrouge.service;

import fr.croixrouge.exposition.dto.core.Donation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Value("${api.url}")
    private String apiUrl;

    final private JavaMailSender mailSender;


    public MailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void sendEmailFromTemplate(String email, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress(from));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Confirmez votre adresse email");


        String htmlTemplate = """
                <!DOCTYPE html>
                <html>
                <head>
                                
                  <meta charset="utf-8">
                  <meta http-equiv="x-ua-compatible" content="ie=edge">
                  <title>Email Confirmation</title>
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <style type="text/css">
                  @media screen {
                    @font-face {
                      font-family: 'Source Sans Pro';
                      font-style: normal;
                      font-weight: 400;
                      src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');
                    }
                    @font-face {
                      font-family: 'Source Sans Pro';
                      font-style: normal;
                      font-weight: 700;
                      src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');
                    }
                  }
                  body,
                  table,
                  td,
                  a {
                    -ms-text-size-adjust: 100%;
                    -webkit-text-size-adjust: 100%;
                  }
                  table,
                  td {
                    mso-table-rspace: 0pt;
                    mso-table-lspace: 0pt;
                  }
                  img {
                    -ms-interpolation-mode: bicubic;
                  }
                  a[x-apple-data-detectors] {
                    font-family: inherit !important;
                    font-size: inherit !important;
                    font-weight: inherit !important;
                    line-height: inherit !important;
                    color: inherit !important;
                    text-decoration: none !important;
                  }
                  div[style*="margin: 16px 0;"] {
                    margin: 0 !important;
                  }
                  body {
                    width: 100% !important;
                    height: 100% !important;
                    padding: 0 !important;
                    margin: 0 !important;
                  }
                  table {
                    border-collapse: collapse !important;
                  }
                  a {
                    color: #1a82e2;
                  }
                  img {
                    height: auto;
                    line-height: 100%;
                    text-decoration: none;
                    border: 0;
                    outline: none;
                  }
                  </style>
                                
                </head>
                <body style="background-color: #e9ecef;">
                  <div class="preheader" style="display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;">
                    Validation d'Email pour une création de compte.
                  </div>
                  <table border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tr>
                      <td align="center" bgcolor="#e9ecef">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                          <tr>
                            <td align="center" valign="top" style="padding: 36px 24px;">
                              <a href="https://pa-crx.fr" target="_blank" style="display: inline-block;">
                                <img src="https://www.larousse.fr/encyclopedie/data/images/1009656-Drapeau_de_la_Croix-Rouge.jpg" alt="Logo" border="0" width="48" style="display: block; width: 48px; max-width: 48px; min-width: 48px;">
                              </a>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td align="center" bgcolor="#e9ecef">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                          <tr>
                            <td align="left" bgcolor="#ffffff" style="padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;">
                              <h1 style="margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;">Confirmez votre adresse email</h1>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td align="center" bgcolor="#e9ecef">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                                
                          <tr>
                            <td align="left" bgcolor="#ffffff" style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;">
                              <p style="margin: 0;">Cliquez sur le bouton ci-dessous pour confirmer votre adresse e-mail. Si vous n'avez pas créé de compte avec <a href="https://pa-crx.fr">PA CRX</a>, vous pouvez supprimer cet e-mail en toute sécurité.</p>
                            </td>
                          </tr>
                                
                          <tr>
                            <td align="left" bgcolor="#ffffff">
                              <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                  <td align="center" bgcolor="#ffffff" style="padding: 12px;">
                                    <table border="0" cellpadding="0" cellspacing="0">
                                      <tr>
                                        <td align="center" bgcolor="#1a82e2" style="border-radius: 6px;">
                                          <a href="${link}" target="_blank" style="display: inline-block; padding: 16px 36px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; color: #ffffff; text-decoration: none; border-radius: 6px;">Confirmer</a>
                                        </td>
                                      </tr>
                                    </table>
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                          <tr>
                            <td align="left" bgcolor="#ffffff" style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;">
                              <p style="margin: 0;">Si cela ne fonctionne pas, copiez et collez le lien suivant dans votre navigateur :</p>
                              <p style="margin: 0;"><a href="${link}" target="_blank">${link}</a></p>
                            </td>
                          </tr>
                                
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td align="center" bgcolor="#e9ecef" style="padding: 24px;">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                                
                          <tr>
                            <td align="center" bgcolor="#e9ecef" style="padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;">
                              <p style="margin: 0;">You received this email because we received a request for account creation. If you didn't request an account creation you can safely delete this email.</p>
                            </td>
                          </tr>
                                
                        </table>
                                
                      </td>
                    </tr>
                                
                  </table>
                                
                </body>
                </html>
                """;

        htmlTemplate = htmlTemplate.replace("${link}", apiUrl + "/create-account/confirm?token=" + token);

        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public void sendEmailForSupport(Donation donation) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress(from));
        message.setRecipients(MimeMessage.RecipientType.TO, donation.getEmail());
        message.setSubject("Merci pour votre don");

        String htmlTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
                <!-- Add your styles or link to external stylesheets here -->
            </head>
            <body style="background-color: #e9ecef;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; border: 1px solid #d4dadf;">
                    <h2 style="text-align: center; color: #1a82e2;">Merci pour votre don, ${donorName}!</h2>
                    <p style="font-size: 16px;">Nous sommes reconnaissants de votre générosité et du soutien que vous apportez à notre cause.</p>
                    <p style="font-size: 16px;">Récapitulatif du don :</p>
                    <ul>
                        <li><strong>Montant du don :</strong> ${donationAmount}</li>
                        <li><strong>Nom :</strong> ${donorName}</li>
                        <li><strong>Email :</strong> ${email}</li>
                        <!-- Add more donor information as needed -->
                    </ul>
                    <p style="font-size: 16px;">Nous apprécions votre engagement envers notre organisation.</p>
                    <p style="font-size: 16px;">Cordialement,</p>
                    <p style="font-size: 16px;">L'équipe de la Croix-Rouge</p>
                </div>
            </body>
            </html>
            """;

        htmlTemplate = htmlTemplate.replace("${donorName}", donation.getFirstName() + " " + donation.getLastName());
        htmlTemplate = htmlTemplate.replace("${donationAmount}", donation.getAmount());
        htmlTemplate = htmlTemplate.replace("${email}", donation.getEmail());


        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        System.out.println("Sending email...");
        try {
        mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Email sent");
        }
    }
}
