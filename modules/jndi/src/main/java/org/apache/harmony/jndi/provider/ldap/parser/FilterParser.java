/* 
 *  Licensed to the Apache Software Foundation (ASF) under one or more 
 *  contributor license agreements.  See the NOTICE file distributed with 
 *  this work for additional information regarding copyright ownership. 
 *  The ASF licenses this file to You under the Apache License, Version 2.0 
 *  (the "License"); you may not use this file except in compliance with 
 *  the License.  You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License. 
 */
/* Generated By:JavaCC: Do not edit this line. FilterParser.java */

package org.apache.harmony.jndi.provider.ldap.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.StringReader;
import org.apache.harmony.jndi.provider.ldap.Filter;
import org.apache.harmony.jndi.internal.parser.AttributeTypeAndValuePair;
import org.apache.harmony.jndi.provider.ldap.Filter.MatchingRuleAssertion;
import org.apache.harmony.jndi.provider.ldap.Filter.SubstringFilter;
import org.apache.harmony.jndi.provider.ldap.asn1.Utils;


/**
 * Ldap filter parser which parse the string representation of Ldap filters to
 * instance of org.apache.harmony.jndi.provider.ldap.filter.Filter according
 * RFC2254. And It also support parse variables of the form {i}.
 * 
 * @see org.apache.harmony.jndi.provider.ldap.filter.Filter
 * @see javax.naming.directory.DirContext#search(javax.naming.Name, String,
 *      Object[], javax.naming.directory.SearchControls)
 */
public class FilterParser implements FilterParserConstants {

  public static void main(String args[]) throws ParseException, FileNotFoundException {
    FilterParser parser = new FilterParser(new FileInputStream("parser.filter.test"));
   //  FilterParser parser = new FilterParser(System.in);
    //System.out.println(parser.value());
      parser.test();
  }

    private Object[] args;

    private boolean isEndEOF = false;

    public FilterParser(String filter) {
        this(new StringReader(filter));
        isEndEOF = true;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    private String getArgAttrValue(String index) {
        int i = Integer.valueOf(index).intValue();
        if (args == null || args.length <= i || args[i] == null) {
            throw new IllegalArgumentException();
        }

        return (String) args[i];
    }

    private String getArgValue(String index) {
        int i = Integer.valueOf(index).intValue();
        if (args == null || args.length <= i || args[i] == null) {
            throw new IllegalArgumentException();
        }
        if (args[i] instanceof String) {
            return (String) args[i];
        }

        //FIXME:
        throw new RuntimeException("Convert value to corresponding java type.");
    }

    private String convertToUtf8Char(String s) {
        byte[] bs = new byte[] { (byte)Integer.parseInt(s, 16) };
        return Utils.getString(bs);
    }

    private Filter parseSubstring(String des, List list) {
        Filter filter = null;
        if (list.size() == 1) {
            if (list.get(0).equals("*")) {
                filter = new Filter(Filter.PRESENT_FILTER);
                filter.setValue(des);
            } else {
                filter = new Filter(Filter.EQUALITY_MATCH_FILTER);
                filter
                        .setValue(new AttributeTypeAndValuePair(des, list
                                .get(0)));
            }
        } else {
            String initial = null;
            String any = "";
            String end = null;
            if (list.get(0).equals("*")) {
                any = "*";
            } else {
                initial = (String) list.get(0);
            }

            for (int i = 1; i < list.size(); ++i) {
                String value = (String) list.get(i);
                if (i == list.size() - 1 && !value.equals("*")) {
                    end = value;
                } else {
                    any += value;
                }
            }
            filter = new Filter(Filter.SUBSTRINGS_FILTER);
            SubstringFilter substring = new SubstringFilter(des);
            if (initial != null) {
                substring.addInitial(initial);
            }

            substring.addAny(any);

            if (end != null) {
                substring.addFinal(end);
            }
            filter.setValue(substring);
        }

        return filter;
    }

  final public String option() throws ParseException {
            StringBuilder value = new StringBuilder();
            Token t;
    t = jj_consume_token(SEMI);
                        value.append(t.image);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case HEX_CHAR:
        t = jj_consume_token(HEX_CHAR);
                             value.append(t.image);
        break;
      case NOHEX_CHAR:
        t = jj_consume_token(NOHEX_CHAR);
                               value.append(t.image);
        break;
      case ZERO:
        t = jj_consume_token(ZERO);
                         value.append(t.image);
        break;
      case DIGIT:
        t = jj_consume_token(DIGIT);
                          value.append(t.image);
        break;
      case HYPHEN:
        t = jj_consume_token(HYPHEN);
                           value.append(t.image);
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case HEX_CHAR:
      case NOHEX_CHAR:
      case ZERO:
      case DIGIT:
      case HYPHEN:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
    }
             {if (true) return value.toString();}
    throw new Error("Missing return statement in function");
  }

  final public String number() throws ParseException {
            StringBuilder value = new StringBuilder();
            Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ZERO:
      t = jj_consume_token(ZERO);
                         value.append(t.image);
      break;
    case DIGIT:
      t = jj_consume_token(DIGIT);
                            value.append(t.image);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ZERO:
        case DIGIT:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_2;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ZERO:
          t = jj_consume_token(ZERO);
                            value.append(t.image);
          break;
        case DIGIT:
          t = jj_consume_token(DIGIT);
                             value.append(t.image);
          break;
        default:
          jj_la1[3] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
             {if (true) return value.toString();}
    throw new Error("Missing return statement in function");
  }

  final public String oid() throws ParseException {
            StringBuilder value = new StringBuilder();
            String number = null;
            Token t = null;
    number = number();
                               value.append(number);
    label_3:
    while (true) {
      t = jj_consume_token(PERIOD);
                               value.append(t.image);
      number = number();
                                    value.append(number);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PERIOD:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_3;
      }
    }
             {if (true) return value.toString();}
    throw new Error("Missing return statement in function");
  }

  final public String matchingrule() throws ParseException {
            String value = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ZERO:
    case DIGIT:
      value = oid();
      break;
    case HEX_CHAR:
    case NOHEX_CHAR:
    case LBRACE:
      value = attrType();
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
            {if (true) return value;}
    throw new Error("Missing return statement in function");
  }

  final public String argument() throws ParseException {
         String num = null;
    jj_consume_token(LBRACE);
    num = number();
    jj_consume_token(RBRACE);
             {if (true) return getArgAttrValue(num);}
    throw new Error("Missing return statement in function");
  }

  final public String attrType() throws ParseException {
            StringBuilder value = new StringBuilder();
            String arg;
            Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case HEX_CHAR:
      t = jj_consume_token(HEX_CHAR);
                             value.append(t.image);
      break;
    case NOHEX_CHAR:
      t = jj_consume_token(NOHEX_CHAR);
                                                                        value.append(t.image);
      break;
    case LBRACE:
      arg = argument();
                                                                                                                   value.append(arg);
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case HEX_CHAR:
      case NOHEX_CHAR:
      case LBRACE:
      case ZERO:
      case DIGIT:
      case HYPHEN:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case HEX_CHAR:
      case NOHEX_CHAR:
      case ZERO:
      case DIGIT:
      case HYPHEN:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case HEX_CHAR:
          t = jj_consume_token(HEX_CHAR);
          break;
        case NOHEX_CHAR:
          t = jj_consume_token(NOHEX_CHAR);
          break;
        case ZERO:
          t = jj_consume_token(ZERO);
          break;
        case DIGIT:
          t = jj_consume_token(DIGIT);
          break;
        case HYPHEN:
          t = jj_consume_token(HYPHEN);
          break;
        default:
          jj_la1[9] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                                                                            value.append(t.image);
        break;
      case LBRACE:
        arg = argument();
                                                                                                                                        value.append(arg);
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
             {if (true) return value.toString();}
    throw new Error("Missing return statement in function");
  }

  final public String attrDescr() throws ParseException {
         String type;
    type = attrType();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SEMI:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_5;
      }
      option();
    }
             {if (true) return type;}
    throw new Error("Missing return statement in function");
  }

  final public void test() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPARENT:
                /*
                 * initial args for test, so there should not be args index larger than
                 * 49 in test case
                 */
                args = new Object[20];
                for (int i = 0; i < args.length; i++) {
                    args[i] = "{" + i + "}";
                }
      parse();
      jj_consume_token(24);
      test();
      break;
    default:
      jj_la1[12] = jj_gen;
      if (jj_2_1(2)) {
        jj_consume_token(24);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 24:
          jj_consume_token(24);
          jj_consume_token(0);
          break;
        case 0:
          jj_consume_token(0);
          break;
        default:
          jj_la1[13] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
  }

// FIXME: get string representation of AttributeValue, then use Rdn.unescapeValue(String) to get value
  final public String value() throws ParseException {
            StringBuilder value = new StringBuilder();
            String hexValue = null;
            Token t = null;;
            String arg = null;
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOHEX_CHAR:
        t = jj_consume_token(NOHEX_CHAR);
                                value.append(t.image);
        break;
      case HEX_CHAR:
        t = jj_consume_token(HEX_CHAR);
                              value.append(t.image);
        break;
      case ZERO:
        t = jj_consume_token(ZERO);
                          value.append(t.image);
        break;
      case DIGIT:
        t = jj_consume_token(DIGIT);
                           value.append(t.image);
        break;
      case HYPHEN:
        t = jj_consume_token(HYPHEN);
                            value.append(t.image);
        break;
      case COLON:
        t = jj_consume_token(COLON);
                           value.append(t.image);
        break;
      case BACKSLASH:
        hexValue = backslashValue();
                                           value.append(hexValue);
        break;
      case LBRACE:
        arg = argument();
                                value.append(arg);
        break;
      case CHAR:
        t = jj_consume_token(CHAR);
                          value.append(t.image);
        break;
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case HEX_CHAR:
      case NOHEX_CHAR:
      case LBRACE:
      case ZERO:
      case COLON:
      case DIGIT:
      case HYPHEN:
      case BACKSLASH:
      case CHAR:
        ;
        break;
      default:
        jj_la1[15] = jj_gen;
        break label_6;
      }
    }
             {if (true) return value.toString();}
    throw new Error("Missing return statement in function");
  }

  final public String backslashValue() throws ParseException {
         String value;
    jj_consume_token(BACKSLASH);
    value = hexDigit();
                                            {if (true) return convertToUtf8Char(value);}
    throw new Error("Missing return statement in function");
  }

  final public String hexDigit() throws ParseException {
            String value;
            Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case HEX_CHAR:
      t = jj_consume_token(HEX_CHAR);
      break;
    case DIGIT:
      t = jj_consume_token(DIGIT);
      break;
    case ZERO:
      t = jj_consume_token(ZERO);
      break;
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                          value = t.image;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case HEX_CHAR:
      t = jj_consume_token(HEX_CHAR);
      break;
    case DIGIT:
      t = jj_consume_token(DIGIT);
      break;
    case ZERO:
      t = jj_consume_token(ZERO);
      break;
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                          value += t.image;
             {if (true) return value;}
    throw new Error("Missing return statement in function");
  }

  final public Filter parse() throws ParseException {
            Filter filter = null;
            Filter temp = null;
    jj_consume_token(LPARENT);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case AND:
      jj_consume_token(AND);
                 filter = new Filter(Filter.AND_FILTER);
      temp = parse();
                 filter.addChild(temp);
      temp = parse();
                 filter.addChild(temp);
      label_7:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LPARENT:
          ;
          break;
        default:
          jj_la1[18] = jj_gen;
          break label_7;
        }
        temp = parse();
                     filter.addChild(temp);
      }
      break;
    case OR:
      jj_consume_token(OR);
                 filter = new Filter(Filter.OR_FILTER);
      temp = parse();
                 filter.addChild(temp);
      temp = parse();
                 filter.addChild(temp);
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LPARENT:
          ;
          break;
        default:
          jj_la1[19] = jj_gen;
          break label_8;
        }
        temp = parse();
                     filter.addChild(temp);
      }
      break;
    case NOT:
      jj_consume_token(NOT);
                 filter = new Filter(Filter.NOT_FILTER);
      temp = parse();
                 filter.setValue(temp);
      break;
    case COLON_DN:
    case HEX_CHAR:
    case NOHEX_CHAR:
    case LBRACE:
    case COLON:
      filter = item();
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    jj_consume_token(RPARENT);
                {if (true) return filter;}
    throw new Error("Missing return statement in function");
  }

  final public void eof() throws ParseException {
    jj_consume_token(0);
  }

  final public Filter item() throws ParseException {
            Filter filter = null;
            String value = null;
            String des = null;
            String temp = null;
            List list = new ArrayList();
            SubstringFilter substring = null;
            MatchingRuleAssertion rule = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case HEX_CHAR:
    case NOHEX_CHAR:
    case LBRACE:
      des = attrDescr();
      if (jj_2_2(2)) {
        jj_consume_token(APPROX);
        jj_consume_token(EQUAL);
        value = value();
                    filter = new Filter(Filter.APPROX_MATCH_FILTER); filter.setValue(new AttributeTypeAndValuePair(des, value));
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case GREATER:
          jj_consume_token(GREATER);
          jj_consume_token(EQUAL);
          value = value();
                    filter = new Filter(Filter.GREATER_OR_EQUAL_FILTER);filter.setValue(new AttributeTypeAndValuePair(des, value));
          break;
        case LESS:
          jj_consume_token(LESS);
          jj_consume_token(EQUAL);
          value = value();
                    filter = new Filter(Filter.LESS_OR_EQUAL_FILTER);filter.setValue(new AttributeTypeAndValuePair(des, value));
          break;
        default:
          jj_la1[22] = jj_gen;
          if (jj_2_3(3)) {
            jj_consume_token(EQUAL);
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case ASTERISK:
              asterisk_start(list);
              break;
            case HEX_CHAR:
            case NOHEX_CHAR:
            case LBRACE:
            case ZERO:
            case COLON:
            case DIGIT:
            case HYPHEN:
            case BACKSLASH:
            case CHAR:
              value_start(list);
              break;
            default:
              jj_la1[21] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
                                                                                   filter = parseSubstring(des, list);
          } else {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case COLON_DN:
            case COLON:
              rule = extensible_1(des);
                                            filter = new Filter(Filter.EXTENSIBLE_MATCH_FILTER); filter.setValue(rule);
              break;
            default:
              jj_la1[23] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
          }
        }
      }
      break;
    case COLON_DN:
    case COLON:
      rule = extensible_2();
                                     filter = new Filter(Filter.EXTENSIBLE_MATCH_FILTER); filter.setValue(rule);
      break;
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
             {if (true) return filter;}
    throw new Error("Missing return statement in function");
  }

  final public void asterisk_start(List list) throws ParseException {
    jj_consume_token(ASTERISK);
                            list.add("*");
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case HEX_CHAR:
    case NOHEX_CHAR:
    case LBRACE:
    case ZERO:
    case COLON:
    case DIGIT:
    case HYPHEN:
    case BACKSLASH:
    case CHAR:
      value_start(list);
      break;
    default:
      jj_la1[25] = jj_gen;
      ;
    }
  }

  final public void value_start(List list) throws ParseException {
                String value;
    value = value();
                                 list.add(value);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ASTERISK:
      asterisk_start(list);
      break;
    default:
      jj_la1[26] = jj_gen;
      ;
    }
  }

  final public String final_part() throws ParseException {
            String value;
    value = value();
             {if (true) return value;}
    throw new Error("Missing return statement in function");
  }

  final public String tail_part() throws ParseException {
            String value;
            String temp;
    value = any_part();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case HEX_CHAR:
    case NOHEX_CHAR:
    case LBRACE:
    case ZERO:
    case COLON:
    case DIGIT:
    case HYPHEN:
    case BACKSLASH:
    case CHAR:
      temp = final_part();
                                                     value = value + temp;
      break;
    default:
      jj_la1[27] = jj_gen;
      ;
    }
             {if (true) return value;}
    throw new Error("Missing return statement in function");
  }

  final public String any_part() throws ParseException {
            StringBuilder value = new StringBuilder();
            Token t;
            String temp;
    t = jj_consume_token(ASTERISK);
                            value.append(t.image);
    label_9:
    while (true) {
      temp = value();
                                          value.append(temp);
      t = jj_consume_token(ASTERISK);
                             value.append(t.image);
      if (jj_2_4(2)) {
        ;
      } else {
        break label_9;
      }
    }
             {if (true) return value.toString();}
    throw new Error("Missing return statement in function");
  }

  final public MatchingRuleAssertion extensible_1(String type) throws ParseException {
            MatchingRuleAssertion rule = new MatchingRuleAssertion();
            rule.setType(type);
            String value;
    if (jj_2_5(2)) {
      jj_consume_token(COLON_DN);
                                      rule.setDnAttributes(true);
    } else {
      ;
    }
    if (jj_2_6(2)) {
      jj_consume_token(COLON);
      value = oid();
                                                 rule.setMatchingRule(value);
    } else {
      ;
    }
    jj_consume_token(COLON);
    jj_consume_token(EQUAL);
    value = value();
                             rule.setMatchValue(value);
             {if (true) return rule;}
    throw new Error("Missing return statement in function");
  }

  final public MatchingRuleAssertion extensible_2() throws ParseException {
            MatchingRuleAssertion rule = new MatchingRuleAssertion();
            String value;
    if (jj_2_7(2)) {
      jj_consume_token(COLON_DN);
    } else {
      ;
    }
    jj_consume_token(COLON);
    value = matchingrule();
                                                                      rule.setMatchingRule(value);
    jj_consume_token(COLON);
    jj_consume_token(EQUAL);
    value = value();
                             rule.setMatchValue(value);
             {if (true) return rule;}
    throw new Error("Missing return statement in function");
  }

  final public void colon_oid() throws ParseException {
    jj_consume_token(COLON);
    oid();
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  final private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  final private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  final private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  final private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  final private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  final private boolean jj_3R_12() {
    Token xsp;
    if (jj_3R_16()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_16()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_15() {
    if (jj_3R_12()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_19()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_32() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_3R_17()) return true;
    return false;
  }

  final private boolean jj_3_6() {
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_13()) return true;
    return false;
  }

  final private boolean jj_3_5() {
    if (jj_scan_token(COLON_DN)) return true;
    return false;
  }

  final private boolean jj_3R_14() {
    if (jj_scan_token(ASTERISK)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_18()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_19() {
    if (jj_3R_14()) return true;
    return false;
  }

  final private boolean jj_3R_33() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(2)) {
    jj_scanpos = xsp;
    if (jj_scan_token(18)) {
    jj_scanpos = xsp;
    if (jj_scan_token(11)) return true;
    }
    }
    return false;
  }

  final private boolean jj_3_3() {
    if (jj_scan_token(EQUAL)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_10()) {
    jj_scanpos = xsp;
    if (jj_3R_11()) return true;
    }
    return false;
  }

  final private boolean jj_3R_11() {
    if (jj_3R_15()) return true;
    return false;
  }

  final private boolean jj_3_4() {
    if (jj_3R_12()) return true;
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  final private boolean jj_3R_13() {
    if (jj_3R_17()) return true;
    return false;
  }

  final private boolean jj_3R_31() {
    if (jj_scan_token(BACKSLASH)) return true;
    if (jj_3R_33()) return true;
    return false;
  }

  final private boolean jj_3R_18() {
    if (jj_3R_15()) return true;
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_scan_token(APPROX)) return true;
    if (jj_scan_token(EQUAL)) return true;
    return false;
  }

  final private boolean jj_3R_28() {
    if (jj_scan_token(CHAR)) return true;
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_scan_token(24)) return true;
    return false;
  }

  final private boolean jj_3R_27() {
    if (jj_3R_32()) return true;
    return false;
  }

  final private boolean jj_3R_30() {
    if (jj_scan_token(DIGIT)) return true;
    return false;
  }

  final private boolean jj_3R_26() {
    if (jj_3R_31()) return true;
    return false;
  }

  final private boolean jj_3R_25() {
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  final private boolean jj_3R_29() {
    if (jj_scan_token(ZERO)) return true;
    return false;
  }

  final private boolean jj_3R_24() {
    if (jj_scan_token(HYPHEN)) return true;
    return false;
  }

  final private boolean jj_3R_23() {
    if (jj_scan_token(DIGIT)) return true;
    return false;
  }

  final private boolean jj_3R_17() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_29()) {
    jj_scanpos = xsp;
    if (jj_3R_30()) return true;
    }
    return false;
  }

  final private boolean jj_3R_22() {
    if (jj_scan_token(ZERO)) return true;
    return false;
  }

  final private boolean jj_3R_21() {
    if (jj_scan_token(HEX_CHAR)) return true;
    return false;
  }

  final private boolean jj_3R_10() {
    if (jj_3R_14()) return true;
    return false;
  }

  final private boolean jj_3_7() {
    if (jj_scan_token(COLON_DN)) return true;
    return false;
  }

  final private boolean jj_3R_20() {
    if (jj_scan_token(NOHEX_CHAR)) return true;
    return false;
  }

  final private boolean jj_3R_16() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) {
    jj_scanpos = xsp;
    if (jj_3R_22()) {
    jj_scanpos = xsp;
    if (jj_3R_23()) {
    jj_scanpos = xsp;
    if (jj_3R_24()) {
    jj_scanpos = xsp;
    if (jj_3R_25()) {
    jj_scanpos = xsp;
    if (jj_3R_26()) {
    jj_scanpos = xsp;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  public FilterParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[28];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0xc080c,0xc080c,0x40800,0x40800,0x40800,0x100000,0x4084c,0x4c,0xc084c,0xc080c,0xc084c,0x400000,0x10,0x1000001,0xac184c,0xac184c,0x40804,0x40804,0x10,0x10,0x174e,0xae184c,0xc000,0x1002,0x104e,0xac184c,0x20000,0xac184c,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[7];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public FilterParser(java.io.InputStream stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new FilterParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public FilterParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new FilterParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public FilterParser(FilterParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(FilterParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 28; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements();) {
        int[] oldentry = (int[])(e.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[25];
    for (int i = 0; i < 25; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 28; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 25; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 7; i++) {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}