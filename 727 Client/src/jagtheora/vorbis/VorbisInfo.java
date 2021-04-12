/* VorbisInfo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package jagtheora.vorbis;

import jagtheora.misc.SimplePeer;
import jagtheora.ogg.OggPacket;

public class VorbisInfo extends SimplePeer {
	public int channels;
	public int rate;

	public VorbisInfo() {
		init();
		if (method6698())
			throw new IllegalStateException();
	}

	private static native void initFields();

	private native void init();

	public native int headerIn(VorbisComment vorbiscomment, OggPacket oggpacket);

	protected native void clear();

	static {
		initFields();
	}

	protected native void q();

	protected native void f();

	protected native void m();
}
