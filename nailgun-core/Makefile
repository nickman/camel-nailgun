# This Makefile has only been tested on linux.  It uses
# MinGW32 to cross-compile for windows.  To install and
# configure MinGW32 on linux, see
# http://www.mingw.org/MinGWiki/index.php/BuildMingwCross
#
# Marty Lamb

CC=gcc
WIN32_CC=c:\MinGW\bin\mingw32-gcc.exe
#WIN32_CC=c:\MinGW\64\x86_64-w64-mingw32\bin\gcc.exe
CFLAGS=-Wall -pedantic -s -O3 
#--host=x86_64-w64-mingw32

ng: src/main/c/ng.c
	@echo "Building ng client.  To build a Windows binary, type 'make ng.exe'"
	${CC} ${CFLAGS} -o ng src/main/c/ng.c

ng.exe: src/main/c/ng.c
	# any idea why the command line is so sensitive to the order of
	# the arguments?  If CFLAGS is at the beginning, it won't link.
	${WIN32_CC} -o ng.exe src/main/c/ng.c -lwsock32 -O3 ${CFLAGS}
	
clean:
	@echo "If you have a Windows binary, 'make clean' won't delete it."
	@echo "You must remove this manually.  Most users won't have MinGW"
	@echo "installed - so I'd rather not delete something they can't rebuild."
	rm ng
#	rm ng.exe
