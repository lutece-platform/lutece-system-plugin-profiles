package fr.paris.lutece.plugins.profiles.business;

import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;

public class ProfileBusinessTest extends LuteceTestCase
{
    private Plugin _plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
    
    public void testCRUD( )
    {
        Profile profile = new Profile( );
        profile.setKey( "key" );
        profile.setDescription( "desc" );
        
        ProfileHome.create( profile, _plugin );
        
        Profile loaded = ProfileHome.findByPrimaryKey( profile.getKey( ), _plugin );
        assertEquals( profile.getDescription( ), loaded.getDescription( ) );
        
        profile.setDescription( "desc2" );
        ProfileHome.update( profile, _plugin );
        
        loaded = ProfileHome.findByPrimaryKey( profile.getKey( ), _plugin );
        assertEquals( profile.getDescription( ), loaded.getDescription( ) );
        
        ProfileHome.remove( profile.getKey( ), _plugin );
        
        loaded = ProfileHome.findByPrimaryKey( profile.getKey( ), _plugin );
        assertNull( loaded );
    }
}
