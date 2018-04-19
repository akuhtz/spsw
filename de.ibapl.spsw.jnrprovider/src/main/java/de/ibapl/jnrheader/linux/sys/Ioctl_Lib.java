package de.ibapl.jnrheader.linux.sys;

import de.ibapl.jnrheader.NativeDataType;
import de.ibapl.jnrheader.Wrapper;
import de.ibapl.jnrheader.posix.sys.Ioctl_H;
import jnr.ffi.LibraryLoader;

public abstract class Ioctl_Lib extends Ioctl_H {
	public static final int TCGETS = 0x5401;
	public static final int TCSETS = 0x5402;
	public static final int TCSETSW = 0x5403;
	public static final int TCSETSF = 0x5404;
	public static final int TCGETA = 0x5405;
	public static final int TCSETA = 0x5406;
	public static final int TCSETAW = 0x5407;
	public static final int TCSETAF = 0x5408;
	public static final int TCSBRK = 0x5409;
	public static final int TCXONC = 0x540A;
	public static final int TCFLSH = 0x540B;
	public static final int TIOCEXCL = 0x540C;
	public static final int TIOCNXCL = 0x540D;
	public static final int TIOCSCTTY = 0x540E;
	public static final int TIOCGPGRP = 0x540F;
	public static final int TIOCSPGRP = 0x5410;
	public static final int TIOCOUTQ = 0x5411;
	public static final int TIOCSTI = 0x5412;
	public static final int TIOCGWINSZ = 0x5413;
	public static final int TIOCSWINSZ = 0x5414;
	public static final int TIOCMGET = 0x5415;
	public static final int TIOCMBIS = 0x5416;
	public static final int TIOCMBIC = 0x5417;
	public static final int TIOCMSET = 0x5418;
	public static final int TIOCGSOFTCAR = 0x5419;
	public static final int TIOCSSOFTCAR = 0x541A;
	public static final int FIONREAD = 0x541B;
	public static final int TIOCINQ = FIONREAD;
	public static final int TIOCLINUX = 0x541C;
	public static final int TIOCCONS = 0x541D;
	public static final int TIOCGSERIAL = 0x541E;
	public static final int TIOCSSERIAL = 0x541F;
	public static final int TIOCPKT = 0x5420;
	public static final int FIONBIO = 0x5421;
	public static final int TIOCNOTTY = 0x5422;
	public static final int TIOCSETD = 0x5423;
	public static final int TIOCGETD = 0x5424;
	public static final int TCSBRKP = 0x5425;
	public static final int TIOCSBRK = 0x5427;
	public static final int TIOCCBRK = 0x5428;
	public static final int TIOCGSID = 0x5429;
	// TODO Linux termios2 ???
	// public static final int TCGETS2 _IOR('T', 0x2A, struct termios2)
	// public static final int TCSETS2 _IOW('T', 0x2B, struct termios2)
	// public static final int TCSETSW2 _IOW('T', 0x2C, struct termios2)
	// public static final int TCSETSF2 _IOW('T', 0x2D, struct termios2)
	public static final int TIOCGRS485 = 0x542E;
	public static final int TIOCSRS485 = 0x542F;
	public static final int TIOCGPTN = 0x80045430; // _IOR('T', 0x30, NativeType.SINT);
	public static final int TIOCSPTLCK = 0x40045431; // _IOW('T', 0x31, NativeType.SINT);
	public static final int TIOCGDEV = 0x80045432; // _IOR('T', 0x32, NativeType.SINT);
	public static final int TCGETX = 0x5432;
	public static final int TCSETX = 0x5433;
	public static final int TCSETXF = 0x5434;
	public static final int TCSETXW = 0x5435;
	public static final int TIOCSIG = 0x40045436; // _IOW('T', 0x36, int32_t);
	public static final int TIOCVHANGUP = 0x5437;
	public static final int TIOCGPKT = 0x80045438; // _IOR('T', 0x38, int32_t);
	public static final int TIOCGPTLCK = 0x80045439; // _IOR('T', 0x39, int32_t);
	public static final int TIOCGEXCL = 0x80045440; // _IOR('T', 0x40, int32_t);
	public static final int TIOCGPTPEER = 0x5441; // _IO('T', 0x41);
	public static final int FIONCLEX = 0x5450;
	public static final int FIOCLEX = 0x5451;
	public static final int FIOASYNC = 0x5452;
	public static final int TIOCSERCONFIG = 0x5453;
	public static final int TIOCSERGWILD = 0x5454;
	public static final int TIOCSERSWILD = 0x5455;
	public static final int TIOCGLCKTRMIOS = 0x5456;
	public static final int TIOCSLCKTRMIOS = 0x5457;
	public static final int TIOCSERGSTRUCT = 0x5458;
	public static final int TIOCSERGETLSR = 0x5459;
	public static final int TIOCSERGETMULTI = 0x545A;
	public static final int TIOCSERSETMULTI = 0x545B;
	public static final int TIOCMIWAIT = 0x545C;
	public static final int TIOCGICOUNT = 0x545D;
	public static final int FIOQSIZE = 0x5460;
	public static final int TIOCPKT_DATA = 0;
	public static final int TIOCPKT_FLUSHREAD = 1;
	public static final int TIOCPKT_FLUSHWRITE = 2;
	public static final int TIOCPKT_STOP = 4;
	public static final int TIOCPKT_START = 8;
	public static final int TIOCPKT_NOSTOP = 16;
	public static final int TIOCPKT_DOSTOP = 32;
	public static final int TIOCPKT_IOCTL = 64;
	public static final int TIOCSER_TEMT = 0x01;
	public static final int SIOCADDRT = 0x890B;
	public static final int SIOCDELRT = 0x890C;
	public static final int SIOCRTMSG = 0x890D;
	public static final int SIOCGIFNAME = 0x8910;
	public static final int SIOCSIFLINK = 0x8911;
	public static final int SIOCGIFCONF = 0x8912;
	public static final int SIOCGIFFLAGS = 0x8913;
	public static final int SIOCSIFFLAGS = 0x8914;
	public static final int SIOCGIFADDR = 0x8915;
	public static final int SIOCSIFADDR = 0x8916;
	public static final int SIOCGIFDSTADDR = 0x8917;
	public static final int SIOCSIFDSTADDR = 0x8918;
	public static final int SIOCGIFBRDADDR = 0x8919;
	public static final int SIOCSIFBRDADDR = 0x891a;
	public static final int SIOCGIFNETMASK = 0x891b;
	public static final int SIOCSIFNETMASK = 0x891c;
	public static final int SIOCGIFMETRIC = 0x891d;
	public static final int SIOCSIFMETRIC = 0x891e;
	public static final int SIOCGIFMEM = 0x891f;
	public static final int SIOCSIFMEM = 0x8920;
	public static final int SIOCGIFMTU = 0x8921;
	public static final int SIOCSIFMTU = 0x8922;
	public static final int SIOCSIFNAME = 0x8923;
	public static final int SIOCSIFHWADDR = 0x8924;
	public static final int SIOCGIFENCAP = 0x8925;
	public static final int SIOCSIFENCAP = 0x8926;
	public static final int SIOCGIFHWADDR = 0x8927;
	public static final int SIOCGIFSLAVE = 0x8929;
	public static final int SIOCSIFSLAVE = 0x8930;
	public static final int SIOCADDMULTI = 0x8931;
	public static final int SIOCDELMULTI = 0x8932;
	public static final int SIOCGIFINDEX = 0x8933;
	public static final int SIOGIFINDEX = SIOCGIFINDEX;
	public static final int SIOCSIFPFLAGS = 0x8934;
	public static final int SIOCGIFPFLAGS = 0x8935;
	public static final int SIOCDIFADDR = 0x8936;
	public static final int SIOCSIFHWBROADCAST = 0x8937;
	public static final int SIOCGIFCOUNT = 0x8938;
	public static final int SIOCGIFBR = 0x8940;
	public static final int SIOCSIFBR = 0x8941;
	public static final int SIOCGIFTXQLEN = 0x8942;
	public static final int SIOCSIFTXQLEN = 0x8943;
	public static final int SIOCDARP = 0x8953;
	public static final int SIOCGARP = 0x8954;
	public static final int SIOCSARP = 0x8955;
	public static final int SIOCDRARP = 0x8960;
	public static final int SIOCGRARP = 0x8961;
	public static final int SIOCSRARP = 0x8962;
	public static final int SIOCGIFMAP = 0x8970;
	public static final int SIOCSIFMAP = 0x8971;
	public static final int SIOCADDDLCI = 0x8980;
	public static final int SIOCDELDLCI = 0x8981;
	public static final int SIOCDEVPRIVATE = 0x89F0;
	public static final int SIOCPROTOPRIVATE = 0x89E0;

	@de.ibapl.jnrheader.NativeFunctions
	protected interface NativeFunctions {
		@NativeDataType("int")
		int ioctl(@NativeDataType("int") int fd, @NativeDataType("unsigned long int") long request);
	}

	@Override
	protected int FIOASYNC() {
		return Ioctl_Lib.FIOASYNC;
	}

	@Override
	protected int FIOCLEX() {
		return Ioctl_Lib.FIOCLEX;
	}

	@Override
	protected int FIONBIO() {
		return Ioctl_Lib.FIONBIO;
	}

	@Override
	protected int FIONCLEX() {
		return Ioctl_Lib.FIONCLEX;
	}

	@Override
	protected int FIONREAD() {
		return Ioctl_Lib.FIONREAD;
	}

	@Override
	protected int FIOQSIZE() {
		return Ioctl_Lib.FIOQSIZE;
	}

	@Override
	protected int SIOCADDDLCI() {
		return Ioctl_Lib.SIOCADDDLCI;
	}

	@Override
	protected int SIOCADDMULTI() {
		return Ioctl_Lib.SIOCADDMULTI;
	}

	@Override
	protected int SIOCADDRT() {
		return Ioctl_Lib.SIOCADDRT;
	}

	@Override
	protected int SIOCDARP() {
		return Ioctl_Lib.SIOCDARP;
	}

	@Override
	protected int SIOCDELDLCI() {
		return Ioctl_Lib.SIOCDELDLCI;
	}

	@Override
	protected int SIOCDELMULTI() {
		return Ioctl_Lib.SIOCDELMULTI;
	}

	@Override
	protected int SIOCDELRT() {
		return Ioctl_Lib.SIOCDELRT;
	}

	@Override
	protected int SIOCDEVPRIVATE() {
		return Ioctl_Lib.SIOCDEVPRIVATE;
	}

	@Override
	protected int SIOCDIFADDR() {
		return Ioctl_Lib.SIOCDIFADDR;
	}

	@Override
	protected int SIOCDRARP() {
		return Ioctl_Lib.SIOCDRARP;
	}

	@Override
	protected int SIOCGARP() {
		return Ioctl_Lib.SIOCGARP;
	}

	@Override
	protected int SIOCGIFADDR() {
		return Ioctl_Lib.SIOCGIFADDR;
	}

	@Override
	protected int SIOCGIFBR() {
		return Ioctl_Lib.SIOCGIFBR;
	}

	@Override
	protected int SIOCGIFBRDADDR() {
		return Ioctl_Lib.SIOCGIFBRDADDR;
	}

	@Override
	protected int SIOCGIFCONF() {
		return Ioctl_Lib.SIOCGIFCONF;
	}

	@Override
	protected int SIOCGIFCOUNT() {
		return Ioctl_Lib.SIOCGIFCOUNT;
	}

	@Override
	protected int SIOCGIFDSTADDR() {
		return Ioctl_Lib.SIOCGIFDSTADDR;
	}

	@Override
	protected int SIOCGIFENCAP() {
		return Ioctl_Lib.SIOCGIFENCAP;
	}

	@Override
	protected int SIOCGIFFLAGS() {
		return Ioctl_Lib.SIOCGIFFLAGS;
	}

	@Override
	protected int SIOCGIFHWADDR() {
		return Ioctl_Lib.SIOCGIFHWADDR;
	}

	@Override
	protected int SIOCGIFINDEX() {
		return Ioctl_Lib.SIOCGIFINDEX;
	}

	@Override
	protected int SIOCGIFMAP() {
		return Ioctl_Lib.SIOCGIFMAP;
	}

	@Override
	protected int SIOCGIFMEM() {
		return Ioctl_Lib.SIOCGIFMEM;
	}

	@Override
	protected int SIOCGIFMETRIC() {
		return Ioctl_Lib.SIOCGIFMETRIC;
	}

	@Override
	protected int SIOCGIFMTU() {
		return Ioctl_Lib.SIOCGIFMTU;
	}

	@Override
	protected int SIOCGIFNAME() {
		return Ioctl_Lib.SIOCGIFNAME;
	}

	@Override
	protected int SIOCGIFNETMASK() {
		return Ioctl_Lib.SIOCGIFNETMASK;
	}

	@Override
	protected int SIOCGIFPFLAGS() {
		return Ioctl_Lib.SIOCGIFPFLAGS;
	}

	@Override
	protected int SIOCGIFSLAVE() {
		return Ioctl_Lib.SIOCGIFSLAVE;
	}

	@Override
	protected int SIOCGIFTXQLEN() {
		return Ioctl_Lib.SIOCGIFTXQLEN;
	}

	@Override
	protected int SIOCGRARP() {
		return Ioctl_Lib.SIOCGRARP;
	}

	@Override
	protected int SIOCPROTOPRIVATE() {
		return Ioctl_Lib.SIOCPROTOPRIVATE;
	}

	@Override
	protected int SIOCRTMSG() {
		return Ioctl_Lib.SIOCRTMSG;
	}

	@Override
	protected int SIOCSARP() {
		return Ioctl_Lib.SIOCSARP;
	}

	@Override
	protected int SIOCSIFADDR() {
		return Ioctl_Lib.SIOCSIFADDR;
	}

	@Override
	protected int SIOCSIFBR() {
		return Ioctl_Lib.SIOCSIFBR;
	}

	@Override
	protected int SIOCSIFBRDADDR() {
		return Ioctl_Lib.SIOCSIFBRDADDR;
	}

	@Override
	protected int SIOCSIFDSTADDR() {
		return Ioctl_Lib.SIOCSIFDSTADDR;
	}

	@Override
	protected int SIOCSIFENCAP() {
		return Ioctl_Lib.SIOCSIFENCAP;
	}

	@Override
	protected int SIOCSIFFLAGS() {
		return Ioctl_Lib.SIOCSIFFLAGS;
	}

	@Override
	protected int SIOCSIFHWADDR() {
		return Ioctl_Lib.SIOCSIFHWADDR;
	}

	@Override
	protected int SIOCSIFHWBROADCAST() {
		return Ioctl_Lib.SIOCSIFHWBROADCAST;
	}

	@Override
	protected int SIOCSIFLINK() {
		return Ioctl_Lib.SIOCSIFLINK;
	}

	@Override
	protected int SIOCSIFMAP() {
		return Ioctl_Lib.SIOCSIFMAP;
	}

	@Override
	protected int SIOCSIFMEM() {
		return Ioctl_Lib.SIOCSIFMEM;
	}

	@Override
	protected int SIOCSIFMETRIC() {
		return Ioctl_Lib.SIOCSIFMETRIC;
	}

	@Override
	protected int SIOCSIFMTU() {
		return Ioctl_Lib.SIOCSIFMTU;
	}

	@Override
	protected int SIOCSIFNAME() {
		return Ioctl_Lib.SIOCSIFNAME;
	}

	@Override
	protected int SIOCSIFNETMASK() {
		return Ioctl_Lib.SIOCSIFNETMASK;
	}

	@Override
	protected int SIOCSIFPFLAGS() {
		return Ioctl_Lib.SIOCSIFPFLAGS;
	}

	@Override
	protected int SIOCSIFSLAVE() {
		return Ioctl_Lib.SIOCSIFSLAVE;
	}

	@Override
	protected int SIOCSIFTXQLEN() {
		return Ioctl_Lib.SIOCSIFTXQLEN;
	}

	@Override
	protected int SIOCSRARP() {
		return Ioctl_Lib.SIOCSRARP;
	}

	@Override
	protected int SIOGIFINDEX() {
		return Ioctl_Lib.SIOGIFINDEX;
	}

	@Override
	protected int TCFLSH() {
		return Ioctl_Lib.TCFLSH;
	}

	@Override
	protected int TCGETA() {
		return Ioctl_Lib.TCGETA;
	}

	@Override
	protected int TCGETS() {
		return Ioctl_Lib.TCGETS;
	}

	@Override
	protected int TCGETX() {
		return Ioctl_Lib.TCGETX;
	}

	@Override
	protected int TCSBRK() {
		return Ioctl_Lib.TCSBRK;
	}

	@Override
	protected int TCSBRKP() {
		return Ioctl_Lib.TCSBRKP;
	}

	@Override
	protected int TCSETA() {
		return Ioctl_Lib.TCSETA;
	}

	@Override
	protected int TCSETAF() {
		return Ioctl_Lib.TCSETAF;
	}

	@Override
	protected int TCSETAW() {
		return Ioctl_Lib.TCSETAW;
	}

	@Override
	protected int TCSETS() {
		return Ioctl_Lib.TCSETS;
	}

	@Override
	protected int TCSETSF() {
		return Ioctl_Lib.TCSETSF;
	}

	@Override
	protected Short TCSETSW() {
		return Ioctl_Lib.TCSETSW;
	}

	@Override
	protected int TCSETX() {
		return Ioctl_Lib.TCSETX;
	}

	@Override
	protected int TCSETXF() {
		return Ioctl_Lib.TCSETXF;
	}

	@Override
	protected int TCSETXW() {
		return Ioctl_Lib.TCSETXW;
	}

	@Override
	protected int TCXONC() {
		return Ioctl_Lib.TCXONC;
	}

	@Override
	protected int TIOCCBRK() {
		return Ioctl_Lib.TIOCCBRK;
	}

	@Override
	protected int TIOCCONS() {
		return Ioctl_Lib.TIOCCONS;
	}

	@Override
	protected int TIOCEXCL() {
		return Ioctl_Lib.TIOCEXCL;
	}

	@Override
	protected int TIOCGDEV() {
		return Ioctl_Lib.TIOCGDEV;
	}

	@Override
	protected int TIOCGETD() {
		return Ioctl_Lib.TIOCGETD;
	}

	@Override
	protected int TIOCGEXCL() {
		return Ioctl_Lib.TIOCGEXCL;
	}

	@Override
	protected int TIOCGICOUNT() {
		return Ioctl_Lib.TIOCGICOUNT;
	}

	@Override
	protected int TIOCGLCKTRMIOS() {
		return Ioctl_Lib.TIOCGLCKTRMIOS;
	}

	@Override
	protected int TIOCGPGRP() {
		return Ioctl_Lib.TIOCGPGRP;
	}

	@Override
	protected int TIOCGPKT() {
		return Ioctl_Lib.TIOCGPKT;
	}

	@Override
	protected int TIOCGPTLCK() {
		return Ioctl_Lib.TIOCGPTLCK;
	}

	@Override
	protected int TIOCGPTN() {
		return Ioctl_Lib.TIOCGPTN;
	}

	@Override
	protected int TIOCGPTPEER() {
		return Ioctl_Lib.TIOCGPTPEER;
	}

	@Override
	protected int TIOCGRS485() {
		return Ioctl_Lib.TIOCGRS485;
	}

	@Override
	protected int TIOCGSERIAL() {
		return Ioctl_Lib.TIOCGSERIAL;
	}

	@Override
	protected int TIOCGSID() {
		return Ioctl_Lib.TIOCGSID;
	}

	@Override
	protected int TIOCGSOFTCAR() {
		return Ioctl_Lib.TIOCGSOFTCAR;
	}

	@Override
	protected int TIOCGWINSZ() {
		return Ioctl_Lib.TIOCGWINSZ;
	}

	@Override
	protected int TIOCINQ() {
		return Ioctl_Lib.TIOCINQ;
	}

	@Override
	protected Short TIOCLINUX() {
		return Ioctl_Lib.TIOCLINUX;
	}

	@Override
	protected int TIOCMBIC() {
		return Ioctl_Lib.TIOCMBIC;
	}

	@Override
	protected int TIOCMBIS() {
		return Ioctl_Lib.TIOCMBIS;
	}

	@Override
	protected int TIOCMGET() {
		return Ioctl_Lib.TIOCMGET;
	}

	@Override
	protected int TIOCMIWAIT() {
		return Ioctl_Lib.TIOCMIWAIT;
	}

	@Override
	protected int TIOCMSET() {
		return Ioctl_Lib.TIOCMSET;
	}

	@Override
	protected int TIOCNOTTY() {
		return Ioctl_Lib.TIOCNOTTY;
	}

	@Override
	protected int TIOCNXCL() {
		return Ioctl_Lib.TIOCNXCL;
	}

	@Override
	protected int TIOCOUTQ() {
		return Ioctl_Lib.TIOCOUTQ;
	}

	@Override
	protected int TIOCPKT() {
		return Ioctl_Lib.TIOCPKT;
	}

	@Override
	protected int TIOCPKT_DATA() {
		return Ioctl_Lib.TIOCPKT_DATA;
	}

	@Override
	protected int TIOCPKT_DOSTOP() {
		return Ioctl_Lib.TIOCPKT_DOSTOP;
	}

	@Override
	protected int TIOCPKT_FLUSHREAD() {
		return Ioctl_Lib.TIOCPKT_FLUSHREAD;
	}

	@Override
	protected int TIOCPKT_FLUSHWRITE() {
		return Ioctl_Lib.TIOCPKT_FLUSHWRITE;
	}

	@Override
	protected int TIOCPKT_IOCTL() {
		return Ioctl_Lib.TIOCPKT_IOCTL;
	}

	@Override
	protected int TIOCPKT_NOSTOP() {
		return Ioctl_Lib.TIOCPKT_NOSTOP;
	}

	@Override
	protected int TIOCPKT_START() {
		return Ioctl_Lib.TIOCPKT_START;
	}

	@Override
	protected int TIOCPKT_STOP() {
		return Ioctl_Lib.TIOCPKT_STOP;
	}

	@Override
	protected int TIOCSBRK() {
		return Ioctl_Lib.TIOCSBRK;
	}

	@Override
	protected int TIOCSCTTY() {
		return Ioctl_Lib.TIOCSCTTY;
	}

	@Override
	protected int TIOCSER_TEMT() {
		return Ioctl_Lib.TIOCSER_TEMT;
	}

	@Override
	protected int TIOCSERCONFIG() {
		return Ioctl_Lib.TIOCSERCONFIG;
	}

	@Override
	protected int TIOCSERGETLSR() {
		return Ioctl_Lib.TIOCSERGETLSR;
	}

	@Override
	protected int TIOCSERGETMULTI() {
		return Ioctl_Lib.TIOCSERGETMULTI;
	}

	@Override
	protected int TIOCSERGSTRUCT() {
		return Ioctl_Lib.TIOCSERGSTRUCT;
	}

	@Override
	protected int TIOCSERGWILD() {
		return Ioctl_Lib.TIOCSERGWILD;
	}

	@Override
	protected int TIOCSERSETMULTI() {
		return Ioctl_Lib.TIOCSERSETMULTI;
	}

	@Override
	protected int TIOCSERSWILD() {
		return Ioctl_Lib.TIOCSERSWILD;
	}

	@Override
	protected int TIOCSETD() {
		return Ioctl_Lib.TIOCSETD;
	}

	@Override
	protected int TIOCSIG() {
		return Ioctl_Lib.TIOCSIG;
	}

	@Override
	protected int TIOCSLCKTRMIOS() {
		return Ioctl_Lib.TIOCSLCKTRMIOS;
	}

	@Override
	protected int TIOCSPGRP() {
		return Ioctl_Lib.TIOCSPGRP;
	}

	@Override
	protected int TIOCSPTLCK() {
		return Ioctl_Lib.TIOCSPTLCK;
	}

	@Override
	protected int TIOCSRS485() {
		return Ioctl_Lib.TIOCSRS485;
	}

	@Override
	protected int TIOCSSERIAL() {
		return Ioctl_Lib.TIOCSSERIAL;
	}

	@Override
	protected int TIOCSSOFTCAR() {
		return Ioctl_Lib.TIOCSSOFTCAR;
	}

	@Override
	protected int TIOCSTI() {
		return Ioctl_Lib.TIOCSTI;
	}

	@Override
	protected int TIOCSWINSZ() {
		return Ioctl_Lib.TIOCSWINSZ;
	}

	@Override
	protected int TIOCVHANGUP() {
		return Ioctl_Lib.TIOCVHANGUP;
	}

	final private NativeFunctions nativeFunctions;

	public Ioctl_Lib() {
		nativeFunctions = LibraryLoader.create(NativeFunctions.class).load("c");
	}

	@Override
	public int ioctl(int fd, long request) {
		return nativeFunctions.ioctl(fd, request);
	}

}
