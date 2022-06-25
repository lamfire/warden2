package com.lamfire.warden.cmd;

import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;

public interface DataCodec {

    byte[] encode(ActionContext context,byte[] data);

    byte[] decode(ActionContext context,byte[] data);
}
