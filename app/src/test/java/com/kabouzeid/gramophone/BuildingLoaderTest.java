//package com.kabouzeid.gramophone;
//
//
//import android.os.Build;
//
//import com.kabouzeid.gramophone.ui.fragments.BuildingLoader;
//import com.kabouzeid.gramophone.utils.loaders.SupportLoaderTestCase;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Matchers;
//import org.mockito.Mockito;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.RuntimeEnvironment;
//import org.robolectric.annotation.Config;
//
//import static org.hamcrest.Matchers.contains;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import java.util.ArrayList;
//
//@RunWith(RobolectricTestRunner.class)
//@Config(manifest = Config.NONE)
//public class BuildingLoaderTest extends SupportLoaderTestCase {
//
//
//    /**
//     * Test that given a single building name, our custom Loader returns an ArrayList
//     * with a single building object.
//     */
//    @Test
//    public void testSingleBuilding() {
//        String[] buildingsToLoad = {"Home"};
//
//        BuildingLoader spyBuildingLoader = setupBuildingLoaderForTest(
//                new BuildingLoader(RuntimeEnvironment.application,
//                        buildingsToLoad));
//
//        ArrayList<Integer> buildings = getLoaderResultSynchronously(spyBuildingLoader);
//
//        Assert.assertNotNull(buildings);
//        assertThat(buildings, contains(42));
//    }
//
//    private BuildingLoader setupBuildingLoaderForTest(BuildingLoader loader) {
//        // Prevent the loader from making request to imaginary REST API to get building info
//        // instead return mocked BuildingModel objects based on the requested building name
//        BuildingLoader spyBuildingLoader = Mockito.spy(
//                loader
//        );
//
//        // Immediately fire the onSuccess callback with a fake BuildingModel
//        // object, just like a real request would.
//        Mockito.doAnswer(invocation -> {
//            String buildingName = (String) invocation.getArguments()[0];
//
////                BuildingModel building = new BuildingModel(buildingName
////                        "Street Address for " + buildingName);
//
//            ((BuildingLoader) invocation.getMock()).onSuccess(42);
//            return null;
//        }).when(spyBuildingLoader).onGetDataForBuilding(Matchers.anyString());
//
//        return spyBuildingLoader;
//    }
//}
