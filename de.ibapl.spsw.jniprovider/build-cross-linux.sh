#!/bin/sh

#autoreconf -i

mvn compile

# "x86_64-linux-gnu"
# "i386-linux-gnu"

# here the compiler produces garbage
# "mipsisa32r6el-linux-gnu"\
# "mipsisa32r6-linux-gnu"\
# "mipsisa64r6el-linux-gnuabi64"\
# "mipsisa64r6-linux-gnuabi64"
# "mips64-linux-gnuabin32"\
# "mips64el-linux-gnuabin32"

for d in\
 "aarch64-linux-gnu"\
 "arm-linux-gnueabi"\
 "arm-linux-gnueabihf"\
 "mips-linux-gnu"\
 "mipsel-linux-gnu"\
 "mips64-linux-gnuabi64"\
 "mips64el-linux-gnuabi64"
do
  ./configure --host=$d --prefix=$PWD/target/$d || exit 1
  make clean || exit 1
  make || exit 1
  make install || exit 1
done


./configure --prefix=$PWD/target || exit 1
make clean || exit 1
make || exit 1
make install || exit 1

mvn install -DskipTests