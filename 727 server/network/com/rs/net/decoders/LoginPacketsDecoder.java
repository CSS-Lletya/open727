package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.cache.io.InputStream;
import com.rs.net.Session;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.utils.AntiFlood;
import com.rs.utils.Encrypt;
import com.rs.utils.IsaacKeyPair;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public final class LoginPacketsDecoder extends Decoder {
	
	public LoginPacketsDecoder(Session session) {
		super(session);
	}

	@Override
	public void decode(InputStream stream) {

	}

	@SuppressWarnings("unused")
	public void decodeWorldLogin(InputStream stream) {

	}
}