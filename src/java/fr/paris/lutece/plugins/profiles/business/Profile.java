/*
 * Copyright (c) 2002-2010, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.profiles.business;

import java.util.List;

import fr.paris.lutece.plugins.profiles.service.ProfilesAdminUserFieldListenerService;
import fr.paris.lutece.portal.service.rbac.RBACResource;

/**
 * 
 * This class provides the object Profile
 *
 */
public class Profile implements RBACResource
{
	public static final String RESOURCE_TYPE = "PROFILES";
	
	private static ProfilesAdminUserFieldListener _profileAdminUserFieldListener;
	private String _strKey;
    private String _strDescription;
    private List<ProfileAction> _listActions;

    /**
     * Init function
     */
    public static void init(  )
    {
    	if ( _profileAdminUserFieldListener == null )
    	{
    		_profileAdminUserFieldListener = new ProfilesAdminUserFieldListener(  );
    		ProfilesAdminUserFieldListenerService.getService(  ).registerListener( _profileAdminUserFieldListener );
    	}
    }
    
    /**
     * Gets the profile key
     * 
     * @return Returns the Key.
     */
    public String getKey(  )
    {
        return _strKey;
    }

    /**
     * Sets the profile key
     *
     * @param strKey The Key
     */
    public void setKey( String strKey )
    {
        _strKey = strKey;
    }

    /**
     * Returns the profile's description
     * 
     * @return Returns the Description.
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the profile's description
     *
     * @param strDescription The profile's description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

	/**
	 * RBAC resource implementation
	 * 
	 * @return The resource type code
	 */
	public String getResourceId(  ) 
	{
		return _strKey;
	}

	/**
     * RBAC resource implementation
     * 
     * @return The resourceId
     */
	public String getResourceTypeCode(  ) 
	{
		return RESOURCE_TYPE;
	}

	/**
	 *
	 * @return a list of action can be use for the profile
	 */
	public List<ProfileAction> getActions(  )
	{
		return _listActions;
	}
	
	/**
	 * set a list of action can be use for the directory
	 * @param profileActions a list of action must be use for the profile
	 */
	public void setActions( List<ProfileAction> profileActions )
	{
		_listActions = profileActions;
	}
}
