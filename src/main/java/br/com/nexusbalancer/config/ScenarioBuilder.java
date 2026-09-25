package br.com.nexusbalancer.config;

import java.util.ArrayList;
import java.util.List;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.vms.VmSimple;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;

public class ScenarioBuilder {

    public static List<HostSimple> createHosts(int numHosts, int hostPes, int hostMips, long hostRam) {
        var hostList = new ArrayList<HostSimple>();
        for (int i = 0; i < numHosts; i++) {
            List<Pe> peList = new ArrayList<>();
            for (int p = 0; p < hostPes; p++) {
                peList.add(new PeSimple(hostMips));
            }
            hostList.add(new HostSimple(hostRam, 10000, 1000000, peList));
        }
        return hostList;
    }

    public static List<VmSimple> createVms(int numVms, int vmPes, int vmMips) {
        var vmList = new ArrayList<VmSimple>();
        for (int i = 0; i < numVms; i++) {
            var vm = new VmSimple(vmMips, vmPes);
            vm.setRam(512).setBw(1000).setSize(10000);
            vmList.add(vm);
        }
        return vmList;
    }

    public static List<CloudletSimple> createCloudlets(int numCloudlets, int cloudletPes, long cloudletLength) {
        var cloudletList = new ArrayList<CloudletSimple>();
        var utilizationModel = new UtilizationModelDynamic(0.5);
        for (int i = 0; i < numCloudlets; i++) {
            var cloudlet = new CloudletSimple(cloudletLength, cloudletPes, utilizationModel);
            cloudlet.setSizes(1024);
            cloudletList.add(cloudlet);
        }
        return cloudletList;
    }
}