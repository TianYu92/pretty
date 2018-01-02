package edu.ecnu.yt.pretty.common.util;

public class SignatureGenerator {
	
	public static String generateParamDescriptor(String methodName, 
			Object[] params) {
		if (params == null || params.length == 0) {return methodName;}
		Class<?>[] types = new Class<?>[params.length];
		for (int i = 0; i < params.length - 1; i++) {
			types[i] = params.getClass();
		}
		return generateParamDescriptor(methodName, types);
	}
	
	public static String generateParamDescriptor(String methodName, 
			Class<?>[] types) {
		if (types == null || types.length == 0) {
			return methodName;
		}
		String[] strs = new String[types.length + 1];
		strs[0] = methodName;		
		for (int i = 0; i < types.length; i++) {
			strs[i+1] = types[i].getName();
		}
		return generate(strs);
	}
	
	public static String generate(String ... strs) {
		StringBuilder builder = new StringBuilder();
		for (String str : strs) {
			builder.append(str).append("$");
		}		
		return builder.toString();
	}
}
