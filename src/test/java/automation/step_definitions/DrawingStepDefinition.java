package automation.step_definitions;

import automation.pages.DrawingPage;
import automation.pages.GeoLocationPage;
import automation.utils.ScenarioContext;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import static org.testng.AssertJUnit.assertNotNull;

@ScenarioScoped
public class DrawingStepDefinition {

    private final Provider<DrawingPage> drawingPageProvider;
    private final Provider<GeoLocationPage> geoLocationPageProvider;

    @Inject
    public DrawingStepDefinition(Provider<DrawingPage> drawingPageProvider, Provider<GeoLocationPage> geoLocationPageProvider) {
        this.drawingPageProvider = drawingPageProvider;
        this.geoLocationPageProvider = geoLocationPageProvider;
    }

    @And("user draws on the drawing pad")
    public void userDrawsOnTheDrawingPad() {
        Assert.assertTrue(drawingPageProvider.get().drow(),
                "Failed to draw on the drawing pad");
    }

    @And("user clicks on drawing page save button")
    public void userClicksOnSaveButton() {
        Assert.assertTrue(drawingPageProvider.get().clickOnSaveButton(),
                "Failed to click on save button");
    }

    @And("user clicks on clear button")
    public void userClicksOnClearButton() {
        Assert.assertTrue(drawingPageProvider.get().clickOnClearButton(),
                "Failed to click on clear button");
    }

    @Then("user should close the save drawing pop up")
    public void userShouldCloseTheSaveDrawingPopUp() {
        Assert.assertTrue(drawingPageProvider.get().saveDrawingPopUp(),
                "Failed to close the save drawing pop up");
    }

    @And("user closes the geo location pop up")
    public void userClosesTheGeoLocationPopUp() {
        Assert.assertTrue(geoLocationPageProvider.get().allowLocationAccess(),
                "Failed to close the geo location pop up");
    }

    @And("user clicks on this link link")
    public void userClicksOnTheThisLinkLink() {
        Assert.assertTrue(geoLocationPageProvider.get().clickOnThisLink(),
                "Failed to click on the this link link");
    }

    @Then("user should see the geo location web page")
    public void userShouldSeeTheGeoLocationPage() {
        Assert.assertTrue(geoLocationPageProvider.get().is404ErrorDisplayed(),
                "Failed to display the geo location page");
    }

    @And("user saves the longitude and latitude data as {string}")
    public void userSavesTheLongitudeAndLatitudeData(String keySuffix) {
        String latitude = geoLocationPageProvider.get().getLatitude();
        String longitude = geoLocationPageProvider.get().getLongitude();

        assertNotNull(latitude, "Latitude data should not be null");
        assertNotNull(longitude, "Longitude data should not be null");

        ScenarioContext.save(keySuffix + "_latitude", latitude);
        ScenarioContext.save(keySuffix + "_longitude", longitude);
    }

    @And("user waits for {int} seconds")
    public void userWaitsForSeconds(int seconds) {
        geoLocationPageProvider.get().waitForSeconds(seconds);
    }

    @And("user clicks on stop observing button")
    public void userClicksOnStopObservingButton() {
        Assert.assertTrue(geoLocationPageProvider.get().clickOnStopObservingButton(),
                "Failed to click on stop observing button");
    }

    @And("user clicks on start observing button")
    public void userClicksOnStartObservingButton() {
        Assert.assertTrue(geoLocationPageProvider.get().clickOnStartObservingButton(),
                "Failed to click on start observing button");
    }

    @Then("user should verify that the longitude and latitude data is not updated after stopping the observation")
    public void userShouldVerifyThatTheLongitudeAndLatitudeDataIsNotUpdatedAfterStoppingTheObservation() {
        String latitudeBefore = ScenarioContext.get("start_latitude", String.class);
        String longitudeBefore = ScenarioContext.get("start_longitude", String.class);

        String latitudeAfter = geoLocationPageProvider.get().getLatitude();
        String longitudeAfter = geoLocationPageProvider.get().getLongitude();

        Assert.assertNotEquals(latitudeBefore, "0", "Latitude should be updated before stopping observation");
        Assert.assertNotEquals(longitudeBefore, "0", "Longitude should be updated before stopping observation");

        Assert.assertEquals(latitudeBefore, latitudeAfter, "Latitude should not be updated after stopping observation");
        Assert.assertEquals(longitudeBefore, longitudeAfter, "Longitude should not be updated after stopping observation");
    }

    @Then("user should verify that the longitude and latitude data is updated after starting the observation")
    public void userShouldVerifyThatTheLongitudeAndLatitudeDataIsUpdatedAfterStartingTheObservation() {
        String latitudeBefore = ScenarioContext.get("start_latitude", String.class);
        String longitudeBefore = ScenarioContext.get("start_longitude", String.class);

        String latitudeAfter = geoLocationPageProvider.get().getLatitude();
        String longitudeAfter = geoLocationPageProvider.get().getLongitude();

        Assert.assertNotEquals(latitudeBefore, latitudeAfter, "Latitude should be updated before starting observation");
        Assert.assertNotEquals(longitudeBefore, longitudeAfter, "Longitude should be updated before starting observation");
    }
}
