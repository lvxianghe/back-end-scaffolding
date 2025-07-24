package org.xiaoxingbomei.common.entity;

/**
 * 状态机
 * 状态、事件、转换、动作
 */
public interface StateMachine<STATE, EVENT>
{
    /**
     * 状态机转移
     */
    public STATE transition(STATE state, EVENT event);

}
