/**
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.fabric.api.jmx;

import org.fusesource.fabric.api.Container;
import org.fusesource.fabric.api.ContainerProvider;
import org.fusesource.fabric.api.CreateContainerMetadata;
import org.fusesource.fabric.api.CreateContainerOptions;
import org.fusesource.fabric.api.FabricRequirements;
import org.fusesource.fabric.api.FabricStatus;
import org.fusesource.fabric.api.Ids;
import org.fusesource.fabric.service.FabricServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
public class FabricManager implements FabricManagerMBean {
    private static final transient Logger LOG = LoggerFactory.getLogger(FabricManager.class);

    private final FabricServiceImpl fabricService;
    private ObjectName objectName;

    public FabricManager(FabricServiceImpl fabricService) {
        this.fabricService = fabricService;
    }

    public ObjectName getObjectName() throws MalformedObjectNameException {
        if (objectName == null) {
            // TODO to avoid mbean clashes if ever a JVM had multiple FabricService instances, we may
            // want to add a parameter of the fabric ID here...
            objectName = new ObjectName("org.fusesource.fabric:type=Fabric");
        }
        return objectName;
    }

    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;
    }

    public void registerMBeanServer(MBeanServer mbeanServer) {
        try {
            ObjectName name = getObjectName();
            ObjectInstance objectInstance = mbeanServer.registerMBean(this, name);
        } catch (Exception e) {
            LOG.warn("An error occured during mbean server registration: " + e, e);
        }
    }

    public void unregisterMBeanServer(MBeanServer mbeanServer) {
        if (mbeanServer != null) {
            try {
                ObjectName name = getObjectName();
                mbeanServer.unregisterMBean(name);
            } catch (Exception e) {
                LOG.warn("An error occured during mbean server registration: " + e, e);
            }
        }
    }

    protected FabricServiceImpl getFabricService() {
        return fabricService;
    }


    // Management API
    //-------------------------------------------------------------------------


    @Override
    public CreateContainerMetadata[] createContainers(CreateContainerOptions options) {
        return getFabricService().createContainers(options);
    }

    @Override
    public ProfileDTO createProfile(String version, String name) {
        return ProfileDTO.newInstance(getFabricService().createProfile(version, name));
    }

    @Override
    public VersionDTO createVersion(String parentVersionId, String toVersion) {
        return VersionDTO.newInstance(getFabricService().createVersion(parentVersionId, toVersion));
    }

    @Override
    public VersionDTO createVersion(String version) {
        return VersionDTO.newInstance(getFabricService().createVersion(version));
    }

    @Override
    public void deleteProfile(String versionId, String profileId) {
        getFabricService().deleteProfile(versionId, profileId);
    }

    @Override
    public void deleteVersion(String version) {
        getFabricService().deleteVersion(version);
    }

    @Override
    public void destroyContainer(String containerId) {
        getFabricService().destroyContainer(containerId);
    }

    @Override
    public ContainerDTO getContainer(String name) {
        return ContainerDTO.newInstance(getFabricService().getContainer(name));
    }

    @Override
    public List<String> getContainerProvisionList(String name) {
        Container container = getFabricService().getContainer(name);
        if (container != null) {
            return new ArrayList<String>();
        }
        return container.getProvisionList();
    }

    @Override
    public List<ContainerDTO> containers() {
        return ContainerDTO.newInstances(getFabricService().getContainers());
    }

/*
    @Override
    public ContainerTemplate getContainerTemplate(Container container, String jmxUser, String jmxPassword) {
        return getFabricService().getContainerTemplate(container, jmxUser, jmxPassword);
    }

*/

    @Override
    public ContainerDTO currentContainer() {
        return ContainerDTO.newInstance(getFabricService().getCurrentContainer());
    }


    @Override
    public String getCurrentContainerName() {
        return getFabricService().getCurrentContainerName();
    }


    @Override
    public String getDefaultJvmOptions() {
        return getFabricService().getDefaultJvmOptions();
    }

    @Override
    public String getDefaultRepo() {
        return getFabricService().getDefaultRepo();
    }


    @Override
    public VersionDTO defaultVersion() {
        return VersionDTO.newInstance(getFabricService().getDefaultVersion());
    }


    @Override
    public FabricStatus fabricStatus() {
        return getFabricService().getFabricStatus();
    }


    @Override
    public String getMavenRepoUploadURI() {
        URI answer = getFabricService().getMavenRepoUploadURI();
        return (answer != null) ? answer.toString() : null;
    }


    @Override
    public String getMavenRepoURI() {
        URI answer = getFabricService().getMavenRepoURI();
        return (answer != null) ? answer.toString() : null;
    }

/*
    
    public PatchService patchService() {
        return getFabricService().getPatchService();
    }
*/


    @Override
    public ProfileDTO getProfile(String version, String name) {
        return ProfileDTO.newInstance(getFabricService().getProfile(version, name));
    }


    @Override
    public List<ProfileDTO> getProfiles(String version) {
        return ProfileDTO.newInstances(getFabricService().getProfiles(version));
    }

    @Override
    public List<String> getProfileIds(String version) {
        return Ids.getIds(getFabricService().getProfiles(version));
    }

/*
    @Override
    public ContainerProvider getProvider(Container container) {
        return getFabricService().getProvider(container);
    }

    @Override
    public ContainerProvider getProvider(String scheme) {
        return getFabricService().getProvider(scheme);
    }

    @Override
    public Map<String, ContainerProvider> providers() {
        return getFabricService().getProviders();
    }
*/


    @Override
    public FabricRequirements requirements() {
        return getFabricService().getRequirements();
    }

    @Override
    public VersionDTO getVersion(String name) {
        return VersionDTO.newInstance(getFabricService().getVersion(name));
    }

    @Override
    public List<VersionDTO> versions() {
        return VersionDTO.newInstances(getFabricService().getVersions());
    }

/*
    public IZKClient getZooKeeper() {
        return getFabricService().getZooKeeper();
    }
*/

    @Override
    public String getZookeeperInfo(String name) {
        return getFabricService().getZookeeperInfo(name);
    }

/*
    public String getZookeeperPassword() {
        return getFabricService().getZookeeperPassword();
    }
*/

    @Override
    public String getZookeeperUrl() {
        return getFabricService().getZookeeperUrl();
    }

    @Override
    public void registerProvider(ContainerProvider provider, Map<String, Object> properties) {
        getFabricService().registerProvider(provider, properties);
    }

    @Override
    public void registerProvider(String scheme, ContainerProvider provider) {
        getFabricService().registerProvider(scheme, provider);
    }


    @Override
    public void setDefaultJvmOptions(String jvmOptions) {
        getFabricService().setDefaultJvmOptions(jvmOptions);
    }

    @Override
    public void setDefaultRepo(String defaultRepo) {
        getFabricService().setDefaultRepo(defaultRepo);
    }


    @Override
    public void setDefaultVersion(String versionId) {
        getFabricService().setDefaultVersion(versionId);
    }


    @Override
    public void requirements(FabricRequirements requirements) throws IOException {
        getFabricService().setRequirements(requirements);
    }

    @Override
    public void startContainer(String containerId) {
        getFabricService().startContainer(containerId);
    }

    @Override
    public void stopContainer(String containerId) {
        getFabricService().stopContainer(containerId);
    }

    @Override
    public void unregisterProvider(ContainerProvider provider, Map<String, Object> properties) {
        getFabricService().unregisterProvider(provider, properties);
    }

    @Override
    public void unregisterProvider(String scheme) {
        getFabricService().unregisterProvider(scheme);
    }
}
