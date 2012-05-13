
package com.github.zoharwolf.kemono.util.resource.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.zoharwolf.kemono.util.collection.FilteredMap;
import com.github.zoharwolf.kemono.util.resource.AbstractDirResource;
import com.github.zoharwolf.kemono.util.resource.DirResource;
import com.github.zoharwolf.kemono.util.resource.Resource;

/**
 * 从文件系统中获取文件夹资源类
 * 
 * @author zohar
 * 
 */

public class FsDirResource extends AbstractDirResource
{
	private String dirPath;
	private String rootPath;
	private Map<String, Resource> existResMap;


	public FsDirResource( File file )
	{
	    String path = file.getPath();
		this.rootPath = pathFix( path );
		init();
	}
	
	private FsDirResource( String rootPath, String path, Map<String, Resource> exsitResMap )
	{
	    this.rootPath = pathFix( rootPath );
	    this.dirPath = pathFix( path );
	    this.existResMap = exsitResMap;
	}
	
	private void init()
    {
	    dirPath = "";
	    existResMap = FilteredMap.lowercasedKeyMap( new HashMap<String, Resource>() );
    }

	@Override
	public Resource get( String path )
	{
		String[] childs = StringUtils.split( path, "/\\", 2 );
		if( childs.length == 0 )
			return this;

		Resource res = getChild( childs[0] );

		if( childs.length == 1 )
			return res;
		if( childs.length == 2 && res instanceof DirResource )
		{
			DirResource dirRes = (DirResource) res;
			return dirRes.get( childs[1] );
		}

		return null;
	}

	private FsFileResource generateFileRes( String path )
	{
	    if (existResMap == null)
	    {
	        existResMap = FilteredMap.lowercasedKeyMap(new HashMap<String, Resource>());
	    }
	    
	    String key = rootPath + "#" + path;
	    
		if( existResMap.containsKey(key.toLowerCase()) )
		{
			return (FsFileResource) existResMap.get( key.toLowerCase() );
		}
		else
		{
		    int index = path.lastIndexOf('/');
		    String parentPath="";
		    if ( index>-1 )
		    {
		        parentPath = path.substring(0, index);
		    }
		    
		    FsDirResource ffrParent = generateDirRes(parentPath); 
			FsFileResource ffr = new FsFileResource(rootPath, path, ffrParent);
			existResMap.put( key.toLowerCase(), ffr );
			return ffr;
		}
	}

	private FsDirResource generateDirRes( String path )
	{
        if (existResMap == null)
        {
            existResMap = FilteredMap.lowercasedKeyMap( new HashMap<String, Resource>() );
        }
        
	    String key = rootPath + "#" + path;
	       
		if( existResMap.containsKey( key.toLowerCase() ) )
		{
			return (FsDirResource) existResMap.get( key.toLowerCase() );
		}
		else
		{
			FsDirResource fdr = new FsDirResource(rootPath, path, existResMap);
			existResMap.put( path.toLowerCase(), fdr );
			
			return fdr;
		}
	}

	@Override
	public String getPath()
	{
		return dirPath;
	}

	@Override
	public DirResource getParent()
	{
	    if ( dirPath==null || dirPath=="" )
	    {
	        return null;
	    }
	    
	    String[] pathArr = StringUtils.split( dirPath, "/\\" );

	    StringBuffer parentPath = new StringBuffer();
	    for ( int i=1; i<pathArr.length-1; i++ )
	    {
	        if ( pathArr[i]==null ) pathArr[i] = "";
	        parentPath.append( pathArr[i] );
	    }
	    
		return generateDirRes( parentPath.toString() );
	}

	private Resource getChild( String path )
	{
		String childPath = rootPath + dirPath + path;
		File f = new File( childPath );
		if( f.isFile() )
		{
			return generateFileRes( dirPath + path );
		}
		else
		{
			return generateDirRes( dirPath + path );
		}
	}

	@Override
	public List<Resource> list()
	{
		File dirFile = new File( rootPath + dirPath );
		File[] fileArr = dirFile.listFiles();

		List<Resource> resList = new ArrayList<Resource>();
		for( File oneFile : fileArr )
		{
		    String fullPath = oneFile.getPath();
		    int startIndex = rootPath.length() + 1;
		    String path = fullPath.substring(startIndex);
			if( oneFile.isFile() )
			{
				resList.add( generateFileRes( path ) );
			}
			else
			{
				resList.add( generateDirRes( path ) );
			}
		}
		return resList;
	}
	
	private String pathFix(String path)
	{
	    if ( path == null || path.isEmpty())
	    {
	        return path;
	    }
	    else if ( !path.endsWith("/") && !path.endsWith("\\") )
	    {
	        return path + "/";
	    }
	    else
	    {
	        return path;
	    }
	}
	
	@Override
	public String toString()
	{
	    return "fs: [root:" + rootPath + "][path:" + dirPath +"]";
	}

}
