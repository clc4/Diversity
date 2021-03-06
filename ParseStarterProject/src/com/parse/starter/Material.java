package com.parse.starter;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Material")
public class Material extends ParseObject {

	public Material() {
	}

	public String getName() {
		return getString("name");
	}
	
	public String getArticle() {
		return getString("article");
	}

	public List<String> getSearchTerms() {
		return getList("searchTerms");
	}
	
	@Override public boolean equals(Object obj) {
	       if (!(obj instanceof Material))
	            return false;
	        if (((Material) obj).getName().equalsIgnoreCase(this.getName())) {
	            return true;
	        }
			return false;
	}
	
	@Override public int hashCode() {
		return getName().toLowerCase().hashCode();
	}

	@Override public String toString() {
		return "Name: " + getName() + " ST: " + getSearchTerms();
	}
}
