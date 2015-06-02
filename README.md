# YourLightMetered
Brand new robust and productive light meter. 
Allows to do reflected and incident metering, provides additional tools making your light metering experience richer.
New algorithms designed to identify, calibre, to use the sensors the most accuratelly.

// version 0.1  2.06.2015

Measurings from front light meter available. App uses the standard light sensor, if available. 

As the input data from the light sensor is measured in LUX measure units, there was a need to find 
formula to convert them into EV (exposure value). On web pages different tables for conversions available. 
As a regression was found the function for converstion. The results are described here[1].
The formula is
  lux = (2 ^ ev) * 2.5;
After some transformations we get the formula:
  ev = Math.log10(0.4 * lux) / Math.log10(2.);

To understand how to operate on these values we need more formulas
Here they are[2]:
EV = log2(N^2/t), 
where:
    N is the relative aperture (f-number)
    t is the exposure time (“shutter speed”) in seconds

Using this formula we find the t and N to use them to relatively calculate our parameters.
    t = N^2 / 2^EV
    N = sqrt(t * 2^EV)

To calculate EV correctly we ajust it along with users ISO value, we use the formula from the source[2]
ev = ev + log(ISO/100)


[1] http://stackoverflow.com/questions/5401738/how-to-convert-between-lux-and-exposure-value
[2] http://en.wikipedia.org/wiki/Exposure_value#EV_as_a_measure_of_luminance_and_illuminance
