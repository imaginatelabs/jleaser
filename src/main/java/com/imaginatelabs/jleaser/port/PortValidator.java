package com.imaginatelabs.jleaser.port;

public class PortValidator {

    public static final String REGEX_ANY_PORT = "\\*";
    public static final String REGEX_PORT_RANGE = "\\d{1,5}-\\d{1,5}";
    public static final String REGEX_PORT_NUMBER = "\\d*";
    public static final String PORT_RANGE_DELIMITER = "-";
    public static final int FULL_PORT_LIMIT_FLOOR = 1;
    public static final int FULL_PORT_LIMIT_CEILING = 65536;
    public static final int DYNAMIC_PORT_LIMIT_CEILING = FULL_PORT_LIMIT_CEILING;
    public static final int DYNAMIC_PORT_LIMIT_FLOOR = 49152;

    public static PortRange parsePortString(String portString) throws PortRangeOutOdBoundsException, PortNumberParseException {
        try {
            if (portString.matches(REGEX_ANY_PORT)) {
                return new PortRange(DYNAMIC_PORT_LIMIT_FLOOR,DYNAMIC_PORT_LIMIT_CEILING);
            } else if (portString.matches(REGEX_PORT_RANGE)){
                String[] range = portString.split(PORT_RANGE_DELIMITER);
                int floor = Integer.parseInt(range[0]);
                int ceiling = Integer.parseInt(range[1]);

                validatePortWithinRange(floor);
                validatePortWithinRange(ceiling);

                if(floor > ceiling){
                    throw new PortRangeOutOdBoundsException(
                            "Port range is invalid %s - range floor %d is larger than range ceiling %d",
                            portString,
                            floor,
                            ceiling
                    );
                }
                return new PortRange(floor,ceiling);

            } else if(portString.matches(REGEX_PORT_NUMBER)) {
                int port = Integer.parseInt(portString);
                validatePortWithinRange(port);
                return new PortRange(port,port);
            }
        } catch (NumberFormatException e) { }
        throw new PortNumberParseException("Port '%s' is not a valid port string", portString);
    }


    public static void validatePortWithinRange(int port) throws PortRangeOutOdBoundsException {
        if (!(port > PortValidator.FULL_PORT_LIMIT_FLOOR && port < PortValidator.FULL_PORT_LIMIT_CEILING)) {
            throw new PortRangeOutOdBoundsException(
                    "Port %d does not fall between %d and %d",
                    port,
                    PortValidator.FULL_PORT_LIMIT_FLOOR,
                    PortValidator.FULL_PORT_LIMIT_CEILING);
        }
    }
}
