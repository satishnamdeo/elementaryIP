/*
 *
 * @date Nov 22, 2012
 *
 * The contents of this file are copyrighted by iLike Technologies Limited, UK. 
 * The contents of this file represents the real and intellectual property of iLike Technologies Limited, UK
 * Any source code, configuration parameters, documentation, 
 * data or database schema may not be copied, modified, 
 * reused or distributed without the written consent of iLike Technologies Limited, UK.
 *
 */
package com.eip.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.codec.Hex;

public class CustomPasswordEncoder extends MessageDigestPasswordEncoder {

	private   String saltValue  ="" ;
	//private   String ENCODING  ="" ; 
   // private   String encrptionAlgorithm  ="256" ;

	/**
	 * Initializes the ShaPasswordEncoder for SHA-1 strength
	 */
	public CustomPasswordEncoder() {
		this(1);
	}

	/**
	 * Initialize the ShaPasswordEncoder with a given SHA stength as supported
	 * by the JVM EX:
	 * <code>ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);</code>
	 * initializes with SHA-256
	 * 
	 * @param strength
	 *            EX: 1, 256, 384, 512
	 */
	public CustomPasswordEncoder(int strength) {
		super("SHA-" + strength);
	}

	public CustomPasswordEncoder(int strength, boolean is64BitEncription) {
		super("SHA-" + strength, is64BitEncription);
	}

	/**
	 * Takes a previously encoded password and compares it with a rawpassword
	 * after mixing in the salt and encoding that value
	 * 
	 * @param encPass
	 *            previously encoded password
	 * @param rawPass
	 *            plain text password
	 * @param salt
	 *            salt to mix into password
	 * @return true or false
	 */
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		String pass1 = "" + encPass;
		String pass2 = encodePassword(rawPass, getSaltValue());
		return pass1.equals(pass2);
	}

	/**
	 * Encodes the rawPass using a MessageDigest. If a salt is specified it will
	 * be merged with the password before encoding.
	 * 
	 * @param rawPass
	 *            The plain text password
	 * @param salt
	 *            The salt to sprinkle
	 * @return Hex string of password digest (or base64 encoded string if
	 *         encodeHashAsBase64 is enabled.
	 */
	public String encodePassword(String rawPass, Object salt) {
		String saltedPass = mergePasswordAndSalt(rawPass, salt, false);

		MessageDigest messageDigest = getMessageDigest();

		byte[] digest;

		try {
			digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported!");
		}

		// // "stretch" the encoded value if configured to do so
		// for (int i = 1; i < iterations; i++) {
		// digest = messageDigest.digest(digest);
		// }

		if (getEncodeHashAsBase64()) {
			return new String(Base64.encode(digest));
		} else {
			return new String(Hex.encode(digest));
		}
	}
	
	/*public boolean  isEqualPassward(String password1,String password2 ) throws Exception {
        MessageDigest msgDigest = null;
        boolean isEqualHash = false ; 
        try {
        	 
        	
            msgDigest = MessageDigest.getInstance(encrptionAlgorithm);
             
            msgDigest.update(password1.getBytes(ENCODING));
            byte rawByte[] = msgDigest.digest();
            
            msgDigest.update(password2.getBytes(ENCODING));
            byte rawByte2[] = msgDigest.digest();
            
            isEqualHash = MessageDigest.isEqual(rawByte, rawByte2);
 
        } catch (NoSuchAlgorithmException e) {
        	
        	e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
        
        	e.printStackTrace();
        }
        return isEqualHash ;
    }*/
 
	public String getSaltValue() {
		return saltValue;
	}

	public void setSaltValue(String saltValue) {
		this.saltValue = saltValue;
	}

}
