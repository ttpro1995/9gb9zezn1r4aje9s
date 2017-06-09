package com.life.db;

import com.life.common.JsonUtils;
import com.life.data.i32db.I32VDB;
import com.life.entity.TokenFirebase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TokenFirebaseDb extends I32VDB<TokenFirebase> {

    private static final Logger LOGGER = LogManager.getLogger(TokenFirebaseDb.class);

    public static TokenFirebaseDb instance = new TokenFirebaseDb("TokenFirebaseDb");

    private TokenFirebaseDb(String name) {
        super(name);
    }

    public void init() {

    }

    public static void main(String[] args) {

        //TokenFirebase token = new TokenFirebase("token test 1");
        int uid = 1;

        //System.err.println("put: " + instance.put(uid, token));
        System.out.println("get: " + instance.get(uid));

        System.exit(0);
    }

}
