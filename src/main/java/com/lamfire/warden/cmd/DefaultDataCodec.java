package com.lamfire.warden.cmd;

import com.lamfire.warden.ActionContext;

class DefaultDataCodec implements DataCodec {
    @Override
    public byte[] encode(ActionContext context, byte[] data) {
        return data;
    }

    @Override
    public byte[] decode(ActionContext context, byte[] data) {
        return data;
    }
}
