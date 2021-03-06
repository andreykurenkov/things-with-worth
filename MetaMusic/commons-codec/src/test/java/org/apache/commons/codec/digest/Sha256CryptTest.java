/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class Sha256CryptTest {

    @Test
    public void testSha256CryptStrings() throws Exception {
        // empty data
        assertEquals("$5$foo$Fq9CX624QIfnCAmlGiPKLlAasdacKCRxZztPoeo7o0B", Crypt.crypt("", "$5$foo"));
        // salt gets cut at dollar sign
        assertEquals("$5$45678$LulJuUIJIn.1uU.KPV9x92umMYFopzVDD.o2ZqA1i2/", Crypt.crypt("secret", "$5$45678"));
        assertEquals("$5$45678$LulJuUIJIn.1uU.KPV9x92umMYFopzVDD.o2ZqA1i2/", Crypt.crypt("secret", "$5$45678$012"));
        assertEquals("$5$45678$LulJuUIJIn.1uU.KPV9x92umMYFopzVDD.o2ZqA1i2/", Crypt.crypt("secret", "$5$45678$012$456"));
        // salt gets cut at maximum length
        assertEquals("$5$1234567890123456$GUiFKBSTUAGvcK772ulTDPltkTOLtFvPOmp9o.9FNPB", Crypt.crypt("secret", "$5$1234567890123456"));
        assertEquals("$5$1234567890123456$GUiFKBSTUAGvcK772ulTDPltkTOLtFvPOmp9o.9FNPB", Crypt.crypt("secret", "$5$1234567890123456789"));
    }

    @Test
    public void testSha256CryptBytes() throws Exception {
        // An empty Bytearray equals an empty String
        assertEquals("$5$foo$Fq9CX624QIfnCAmlGiPKLlAasdacKCRxZztPoeo7o0B", Crypt.crypt(new byte[0], "$5$foo"));
        // UTF-8 stores \u00e4 "a with diaeresis" as two bytes 0xc3 0xa4.
        assertEquals("$5$./$iH66LwY5sTDTdHeOxq5nvNDVAxuoCcyH/y6Ptte82P8", Crypt.crypt("t\u00e4st", "$5$./$"));
        // ISO-8859-1 stores "a with diaeresis" as single byte 0xe4.
        assertEquals("$5$./$qx5gFfCzjuWUOvsDDy.5Nor3UULPIqLVBZhgGNS0c14", Crypt.crypt("t\u00e4st".getBytes("ISO-8859-1"), "$5$./$"));
    }

    @Test
    public void testSha256CryptExplicitCall() throws Exception {
        assertTrue(Sha2Crypt.sha256Crypt("secret".getBytes()).matches("^\\$5\\$[a-zA-Z0-9./]{0,16}\\$.{1,}$"));
        assertTrue(Sha2Crypt.sha256Crypt("secret".getBytes(), null).matches("^\\$5\\$[a-zA-Z0-9./]{0,16}\\$.{1,}$"));
    }

    @Test(expected = NullPointerException.class)
    public void testSha256CryptNullData() throws Exception {
        Sha2Crypt.sha256Crypt((byte[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSha256CryptWithEmptySalt() throws Exception {
        Sha2Crypt.sha256Crypt("secret".getBytes(), "");
    }
}
