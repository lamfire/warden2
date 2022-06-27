package com.lamfire.warden.cmd;

import com.lamfire.warden.ActionContext;

public interface CmdCodec<T> {

    CmdREQ<T> parseCmd(ActionContext context, byte[] data);

    byte[] decode(ActionContext context);

    byte[] encode(ActionContext context,byte[] data);

}
