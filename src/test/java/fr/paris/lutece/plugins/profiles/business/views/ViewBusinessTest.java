/*
 * Copyright (c) 2002-2022, City of Paris
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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Tests the assignment of profiles to views
 */
public class ViewBusinessTest extends LuteceTestCase
{
    private static final String VIEW_KEY = "view_test_has_view";
    private static final String PROFILE_KEY = "profile_test_has_view";

    /**
     * A profile assigned to a view has a view, whatever the key of this view: hasView reads the profile key, not the view key
     */
    @Test
    void testHasViewReadsTheProfileKey( )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
        View view = new View( );
        view.setKey( VIEW_KEY );
        view.setDescription( "desc" );
        ViewHome.create( view, plugin );
        try
        {
            assertFalse( ViewHome.hasView( PROFILE_KEY, plugin ) );

            ViewHome.addProfileForView( VIEW_KEY, PROFILE_KEY, plugin );
            assertTrue( ViewHome.hasView( PROFILE_KEY, plugin ) );
            assertFalse( ViewHome.hasView( VIEW_KEY, plugin ) );
        }
        finally
        {
            ViewHome.removeProfiles( VIEW_KEY, plugin );
            ViewHome.remove( VIEW_KEY, plugin );
        }
    }
}
