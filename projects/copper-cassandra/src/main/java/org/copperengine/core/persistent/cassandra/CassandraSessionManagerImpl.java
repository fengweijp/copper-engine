package org.copperengine.core.persistent.cassandra;

import java.util.Collection;

import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;

public class CassandraSessionManagerImpl implements CassandraSessionManager {

    private static final Logger logger = LoggerFactory.getLogger(CassandraSessionManagerImpl.class);

    private final String keyspace;
    private final Collection<String> hosts;
    private final Integer port;
    private Cluster cassandraCluster;
    private Session session;

    public CassandraSessionManagerImpl(Collection<String> hosts, Integer port, String keyspace) {
        if (hosts == null || hosts.isEmpty())
            throw new NullArgumentException("hosts");
        if (keyspace == null || keyspace.isEmpty())
            throw new NullArgumentException("keyspace");
        this.hosts = hosts;
        this.port = port;
        this.keyspace = keyspace;
    }

    public synchronized void startup() {
        if (cassandraCluster != null)
            return;

        Builder b = Cluster.builder();
        b.withLoadBalancingPolicy(new TokenAwarePolicy(new DCAwareRoundRobinPolicy()));
        for (String host : hosts) {
            b.addContactPoint(host);
        }
        if (port != null) {
            b.withPort(port);
        }
        cassandraCluster = b.build();

        logger.info("Connected to cluster: {}", cassandraCluster.getMetadata().getClusterName());
        for (Host host : cassandraCluster.getMetadata().getAllHosts()) {
            logger.info("Datatacenter: {} Host: {} Rack: {}", host.getDatacenter(), host.getAddress(), host.getRack());
        }

        session = cassandraCluster.connect(keyspace);
    }

    public synchronized void shutdown() {
        cassandraCluster.close();
    }

    @Override
    public Session getSession() {
        return session;
    }

}