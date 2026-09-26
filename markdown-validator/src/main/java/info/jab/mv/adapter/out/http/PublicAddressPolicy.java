package info.jab.mv.adapter.out.http;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.function.Predicate;

/**
 * Allows only destinations whose host resolves exclusively to public addresses.
 * <p>
 * Loopback, wildcard, link-local (including cloud metadata endpoints), private (RFC 1918),
 * carrier-grade NAT, IPv6 unique-local and multicast addresses are rejected so link validation
 * cannot be used to probe internal networks.
 */
final class PublicAddressPolicy implements Predicate<URI> {

    @Override
    public boolean test(URI uri) {
        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            return false;
        }
        try {
            for (InetAddress address : InetAddress.getAllByName(host)) {
                if (!isPublic(address)) {
                    return false;
                }
            }
            return true;
        } catch (UnknownHostException e) {
            // Unresolvable hosts cannot reach internal services; let the HTTP client report the failure.
            return true;
        }
    }

    static boolean isPublic(InetAddress address) {
        if (address.isAnyLocalAddress()
                || address.isLoopbackAddress()
                || address.isLinkLocalAddress()
                || address.isSiteLocalAddress()
                || address.isMulticastAddress()) {
            return false;
        }
        byte[] bytes = address.getAddress();
        if (address instanceof Inet4Address) {
            int first = bytes[0] & 0xFF;
            int second = bytes[1] & 0xFF;
            boolean thisNetwork = first == 0;
            boolean carrierGradeNat = first == 100 && second >= 64 && second <= 127;
            return !thisNetwork && !carrierGradeNat;
        }
        if (address instanceof Inet6Address) {
            boolean uniqueLocal = (bytes[0] & 0xFE) == 0xFC;
            return !uniqueLocal;
        }
        return true;
    }
}
