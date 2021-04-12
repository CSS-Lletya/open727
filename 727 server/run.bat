@echo off
@title RUN MATRIX
java -XX:-OmitStackTraceInFastThrow -server -Xms2048m -Xmx4096m -cp bin;data/libs/netty-3.5.2.Final.jar;data/libs/FileStore.jar com.rs.Launcher false false true
pause