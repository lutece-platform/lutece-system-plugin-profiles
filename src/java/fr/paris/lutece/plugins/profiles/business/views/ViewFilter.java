/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.profiles.business.views;

import fr.paris.lutece.plugins.profiles.utils.constants.ProfilesConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * ViewFilter
 *
 */
public class ViewFilter
{
    private String _strKey;
    private String _strDescription;

    /**
     * Initialize each component of the object
     */
    public void init( )
    {
        _strKey = StringUtils.EMPTY;
        _strDescription = StringUtils.EMPTY;
    }

    /**
     * Gets the view key
     * 
     * @return Returns the Key.
     */
    public String getKey( )
    {
        return _strKey;
    }

    /**
     * Sets the view key
     *
     * @param strKey
     *            The Key
     */
    public void setKey( String strKey )
    {
        _strKey = strKey;
    }

    /**
     * Returns the view's description
     * 
     * @return Returns the Description.
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the view's description
     *
     * @param strDescription
     *            The view's description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Set the value of the ViewFilter
     * 
     * @param request
     *            HttpServletRequest
     * @return true if there is a search
     */
    public boolean setFilter( HttpServletRequest request )
    {
        boolean bIsSearch = false;
        String strIsSearch = request.getParameter( ProfilesConstants.PARAMETER_SEARCH_IS_SEARCH );

        if ( strIsSearch != null )
        {
            bIsSearch = true;
            _strKey = request.getParameter( ProfilesConstants.PARAMETER_SEARCH_KEY );
            _strDescription = request.getParameter( ProfilesConstants.PARAMETER_SEARCH_DESCRIPTION );
        }
        else
        {
            init( );
        }

        return bIsSearch;
    }

    /**
     * Build url attributes
     * 
     * @param url
     *            the url
     */
    public void setUrlAttributes( UrlItem url )
    {
        url.addParameter( ProfilesConstants.PARAMETER_SEARCH_IS_SEARCH, Boolean.TRUE.toString( ) );

        try
        {
            url.addParameter( ProfilesConstants.PARAMETER_SEARCH_KEY,
                    URLEncoder.encode( _strKey, AppPropertiesService.getProperty( ProfilesConstants.PROPERTY_ENCODING_URL ) ) );
            url.addParameter( ProfilesConstants.PARAMETER_SEARCH_DESCRIPTION,
                    URLEncoder.encode( _strDescription, AppPropertiesService.getProperty( ProfilesConstants.PROPERTY_ENCODING_URL ) ) );
        }
        catch( UnsupportedEncodingException e )
        {
            AppLogService.error( e );
        }
    }

    /**
     * Build url attributes
     * 
     * @return the url attributes
     */
    public String getUrlAttributes( )
    {
        StringBuilder sbUrlAttributes = new StringBuilder( );
        sbUrlAttributes.append( ProfilesConstants.PARAMETER_SEARCH_IS_SEARCH + ProfilesConstants.EQUAL + Boolean.TRUE );

        try
        {
            sbUrlAttributes.append( ProfilesConstants.AMPERSAND + ProfilesConstants.PARAMETER_SEARCH_KEY + ProfilesConstants.EQUAL
                    + URLEncoder.encode( _strKey, AppPropertiesService.getProperty( ProfilesConstants.PROPERTY_ENCODING_URL ) ) );
            sbUrlAttributes.append( ProfilesConstants.AMPERSAND + ProfilesConstants.PARAMETER_SEARCH_DESCRIPTION + ProfilesConstants.EQUAL
                    + URLEncoder.encode( _strDescription, AppPropertiesService.getProperty( ProfilesConstants.PROPERTY_ENCODING_URL ) ) );
        }
        catch( UnsupportedEncodingException e )
        {
            AppLogService.error( e );
        }

        return sbUrlAttributes.toString( );
    }
}
