package com.life.app.test;

import com.life.socket.SocketClientManager;

/**
 *
 *
 * @author truongnguyenax@gmail.com
 */
public class Test_ReceiveNotify {

	public static void main(String[] args) throws InterruptedException {
        SocketClientManager.token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0cnVvbmduZ3V5ZW5heDFAZ21haWwuY29tIiwiaWF0IjoxNDkzODEzOTczLCJzdWIiOiJUb2tlbiIsImlzcyI6IkxpZmUzNjAiLCJleHAiOjIxMjQ1MzM5NzN9.lq9cOk_iu8XhXhCm_knsGTCwOsqsFW7rp2yrxgi31to";
        //SocketClientManager.token ="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0ZXN0bWFpbDFAZ21haWwuY29tIiwiaWF0IjoxNDk0MDU3NTEyLCJzdWIiOiJUb2tlbiIsImlzcyI6IkxpZmUzNjAiLCJleHAiOjIxMjQ3Nzc1MTJ9.8yK6sN3bAbbgifBhoqodvaXWBGV7gGpSfVYeS6C-fu8";
		SocketClientManager.login();
        
        SocketClientManager.getResult();
		System.exit(0);

	}

}
