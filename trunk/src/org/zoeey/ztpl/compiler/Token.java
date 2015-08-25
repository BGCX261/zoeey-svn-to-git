/*
 * MoXie (SysTem128@GMail.Com) 2010-1-21 10:23:00
 * $Id$
 * 
 * Copyright &copy; 2008-2010 Zoeey.Org . All rights are reserved.
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

/**
 * 标识
 * @author MoXie
 */
public class Token {

    private TokenType type = null;
    private String words = null;
    private int priority = 0;

    /**
     * 标识
     * @param type  类型
     * @param words  词组
     * @param operatorCount 目数（参与操作参数个数如：unaru,binaru,ternary operator...）
     * @param priority 操作符优先级
     */
    Token(TokenType type, String words, int priority) {
        this.type = type;
        this.words = words;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public TokenType getType() {
        return type;
    }

    public String getWords() {
        return words;
    }
}
