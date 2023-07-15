package com.Xogito.Assignment.Utilities;

/**
 * Class containing  {@link com.fasterxml.jackson.annotation.JsonView @JsonView} jsonView indicators  
 */
public class Views {
	
	public interface coreDataView {};
	
	public interface fullDataView extends coreDataView {};

}
