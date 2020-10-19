package net.class101.homework1.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExHashMap extends HashMap<String, Object> {

	private static final long serialVersionUID = -1243891433232523838L;

	public static ExHashMap newInstance(Map<String, Object> map) {
		return new ExHashMap(map);
	}
	
	public ExHashMap() {
		super();
	}
	
	public ExHashMap(String requestCode, int resultCode) {
		super();
		putRequestCode(requestCode);
		putResultCode(resultCode);
		/* epopconcdh */
		put("result", resultCode);
		/* /epopconcdh */
	}
	
	public ExHashMap(Map<String, Object> map) {
		super(map);
	}
	
	/* epopconcdh */
	public ExHashMap(Map<String, Object> map, String requestCode) {
		super(map);
		putRequestCode(requestCode);
	}
	/* /epopconcdh */
	
	public ExHashMap(Map<String, Object> map, String requestCode, int resultCode) {
		super(map);
		putRequestCode(requestCode);
		putResultCode(resultCode);
	}
	
	public ExHashMap putResultCode(int resultCode) {
		put("resultCode", resultCode);
		return this;
	}
	
	public ExHashMap putRequestCode(String requestCode) {
		put("requestCode", requestCode);
		return this;
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public int getInt(String key, int def) {
		try {
			Object o = get(key);
			if (o == null) return def;
			
			if (o instanceof Number) {
				Number n = (Number) o;
				return n.intValue();
			}
			else {
				return Integer.parseInt(getString(key));
			}
		}
		catch (Exception e) {
//			e.printStackTrace();
			return def;
		}
	}
	
	public String getString(String key) {
		return getString(key, "");
	}
	
	public String getString(String key, String def) {
		try {
			Object o = get(key);
			if (o == null) return def;
			
			if (o instanceof String) {
				return (String) o;
			}
			else {
				return o.toString();
			}
		}
		catch (Exception e) {
			return def;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ExHashMap getMap(String key) {
		Object val = get(key);
		if (val == null) return null;
		
		if (val instanceof ExHashMap) {
			return (ExHashMap) val; 
		}
		
		// Reference 유지를 위해 새로 ExHashMap 형태로 넣어주고 return
		ExHashMap newVal = ExHashMap.newInstance((Map<String, Object>)val);
		put(key, newVal);
		
		return newVal;
	}
	
	@SuppressWarnings("unchecked")
	public List<ExHashMap> getMapArray(String key) {
		Object val = get(key);
		if (val == null) return null;
		
		List<Object> list = (List<Object>) val;
		if (list.size() == 0) return (List<ExHashMap>) val;
		
		// type 확인
		Object o1 = list.get(0);
		if (o1 instanceof ExHashMap) {
			return (List<ExHashMap>) val;
		}
		
		// 새로 add		
		ArrayList<ExHashMap> newList = new ArrayList<>();
		for (Map<String, Object> item : (List<Map<String, Object>>)val) {
			newList.add(ExHashMap.newInstance(item));
		}
		
		put(key, newList);
		
		return newList;
	}
	
	public ExHashMap addObject(String key, Object value) {
		put(key, value);
		return this;
	}
	
	private static ArrayList<ExHashMap> joinMap(ExHashMap firstItem, List<ExHashMap> second,
			String joinKeys[], JoinType joinType) {
		
		ArrayList<ExHashMap> joins = new ArrayList<>();

		// second의 각 row 확인
		for (ExHashMap secondItem : second) {
			boolean found = true; // join 가능 여부
			
			for (String joinKey : joinKeys) { // key 별로 확인
			    if (firstItem.containsKey(joinKey) && secondItem.containsKey(joinKey)) {
			    	Object firstValue = firstItem.get(joinKey);
			    	Object secondValue = secondItem.get(joinKey);
			    	
			    	if (firstValue == null || secondValue == null || !firstValue.equals(secondValue)) { // join 할 수 없음.
			    		found = false;
			    		break;
			    	}
			    }
			    else { // key를 갖고 있지 않음. stop
			    	found = false;
			    	break;
			    }
			}
			
			if (found) {
				ExHashMap join = new ExHashMap();
				join.putAll(secondItem);				
				join.putAll(firstItem);
				
				joins.add(join);
			}
		}
		
		if (joins.size() == 0 && joinType == JoinType.OUTER_JOIN) { // join된 항목이 없고, outer join 인 경우
			ExHashMap join = new ExHashMap();
			
			// null value를 가진 key만 입력한다.
			if (second.size() > 0 && second.get(0) != null) {
				for (String key : second.get(0).keySet()) {
					join.put(key, "");
				}
			}
			
			// first를 넣어 준다. 
			join.putAll(firstItem);
			joins.add(join);
		}
		
		return joins;
	}

	public enum JoinType { 
		JOIN,
		OUTER_JOIN
	}
	
	/**
	 * Join Map
	 * @param first first map list
	 * @param second second map list
	 * @param joinKeys sql join 시 on 구문에 들어가는 field name의 array
	 * @param joinType join, outer join 구분
	 * @return
	 */
	public static List<ExHashMap> joinListMap(List<ExHashMap> first, List<ExHashMap> second,
			String[] joinKeys, JoinType joinType) {
		ArrayList<ExHashMap> ret = new ArrayList<>();
		
		if (first == null) return ret;
		if (second == null || second.size() == 0) return first;
		
		for (ExHashMap firstItem : first) {
			ArrayList<ExHashMap> join = joinMap(firstItem, second, joinKeys, joinType);
			if (join.size() > 0) {
				ret.addAll(join);
			}
		}
		
		return ret;
	}
	
	public static ExHashMap convertValueTypeString(ExHashMap param) {
		ExHashMap ret = new ExHashMap(param);
		for (Entry<String, Object> entry : param.entrySet()) {
			Object o = entry.getValue();
			String s = (o instanceof String) ? (String)o : o.toString();
			ret.put(entry.getKey(), s);
		}
		return ret;
	}
}
