package br.com.vittasync.vittasync.Util;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import
        java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);

            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao encriptar senha", e);
        }
    }
}
