/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace1\\JSBD_ZT_Voice\\src\\com\\jsbd\\jsbdvoice\\service\\JSBDTtsService.aidl
 */
package com.jsbd.jsbdvoice.service;
public interface JSBDTtsService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.jsbd.jsbdvoice.service.JSBDTtsService
{
private static final java.lang.String DESCRIPTOR = "com.jsbd.jsbdvoice.service.JSBDTtsService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.jsbd.jsbdvoice.service.JSBDTtsService interface,
 * generating a proxy if needed.
 */
public static com.jsbd.jsbdvoice.service.JSBDTtsService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.jsbd.jsbdvoice.service.JSBDTtsService))) {
return ((com.jsbd.jsbdvoice.service.JSBDTtsService)iin);
}
return new com.jsbd.jsbdvoice.service.JSBDTtsService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_registerTtsListener:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.jsbd.jsbdvoice.service.JSBDTtsListener _arg1;
_arg1 = com.jsbd.jsbdvoice.service.JSBDTtsListener.Stub.asInterface(data.readStrongBinder());
this.registerTtsListener(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterTtsListener:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.jsbd.jsbdvoice.service.JSBDTtsListener _arg1;
_arg1 = com.jsbd.jsbdvoice.service.JSBDTtsListener.Stub.asInterface(data.readStrongBinder());
this.unregisterTtsListener(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_startSpeak:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.startSpeak(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_pauseSpeak:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.pauseSpeak(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stopSpeak:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.stopSpeak(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_resumeSpeak:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.resumeSpeak(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.jsbd.jsbdvoice.service.JSBDTtsService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void registerTtsListener(java.lang.String flag, com.jsbd.jsbdvoice.service.JSBDTtsListener cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(flag);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerTtsListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterTtsListener(java.lang.String flag, com.jsbd.jsbdvoice.service.JSBDTtsListener cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(flag);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterTtsListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startSpeak(java.lang.String flag, java.lang.String text) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(flag);
_data.writeString(text);
mRemote.transact(Stub.TRANSACTION_startSpeak, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void pauseSpeak(java.lang.String flag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(flag);
mRemote.transact(Stub.TRANSACTION_pauseSpeak, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopSpeak(java.lang.String flag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(flag);
mRemote.transact(Stub.TRANSACTION_stopSpeak, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void resumeSpeak(java.lang.String flag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(flag);
mRemote.transact(Stub.TRANSACTION_resumeSpeak, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerTtsListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterTtsListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_startSpeak = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_pauseSpeak = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_stopSpeak = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_resumeSpeak = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void registerTtsListener(java.lang.String flag, com.jsbd.jsbdvoice.service.JSBDTtsListener cb) throws android.os.RemoteException;
public void unregisterTtsListener(java.lang.String flag, com.jsbd.jsbdvoice.service.JSBDTtsListener cb) throws android.os.RemoteException;
public void startSpeak(java.lang.String flag, java.lang.String text) throws android.os.RemoteException;
public void pauseSpeak(java.lang.String flag) throws android.os.RemoteException;
public void stopSpeak(java.lang.String flag) throws android.os.RemoteException;
public void resumeSpeak(java.lang.String flag) throws android.os.RemoteException;
}
