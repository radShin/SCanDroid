/*
 *
 * Copyright (c) 2009-2012,
 *
 *  Galois, Inc. (Aaron Tomb <atomb@galois.com>, Rogan Creswick <creswick@galois.com>)
 *  Steve Suh    <suhsteve@gmail.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The names of the contributors may not be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *
 */
package org.scandroid.testing;

public class ConstructorArgFlow {
	public static class Id {
		Object o;
		
		public Id() {
			
		}

		public Id(Object o) {
			this.o = o;
		}
		
		public void setO(Object o) {
			this.o = o;
		}

		public Object getO() {
			return this.o;
		}
	}

	public static Id flow(String s) {
		return new Id(s);
	}
	
	public static Id flow2(String s) {
		Id id = new Id(s);
		return id;
	}
	
	public static Id manualFlow(String s) {
		// TODO: refactor compilation of flowtypes such that
		// if RHS flowtype SSA val chain does *not* include SSA val of LHS,
		// then we emit some SSA instruction to connect the two 
		Id id = new Id();
		id.setO(s);
		return id;
	}
	
	public static Object fieldAccessFlow(Id id) {
		// TODO: bug in argument of return
		return id.o;
	}
	
	public static Object getterAccessFlow(Id id) {
		// TODO: correct emission of call tag will fix this
		return id.getO();
	}
}
