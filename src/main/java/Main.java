import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.VmSimple;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("     SIMULADOR CLOUDSIM PLUS - INTERFACE CLI      ");
        System.out.println("==================================================\n");

        System.out.print("Qtd de Hosts: ");
        int numHosts = scanner.nextInt();

        System.out.print("Cores por Host: ");
        int hostPes = scanner.nextInt();

        System.out.print("MIPS por Core: ");
        int hostMips = scanner.nextInt();

        System.out.print("RAM por Host (MB): ");
        long hostRam = scanner.nextLong();

        System.out.print("\nQtd de VMs: ");
        int numVms = scanner.nextInt();

        System.out.print("Cores por VM: ");
        int vmPes = scanner.nextInt();

        System.out.print("\nQtd de Cloudlets (Tarefas): ");
        int numCloudlets = scanner.nextInt();

        System.out.print("Tamanho do Cloudlet (MI): ");
        long cloudletLength = scanner.nextLong();

        System.out.println("\nIniciando simulação...\n");

        var simulation = new CloudSimPlus();

        var hostList = new ArrayList<HostSimple>();
        for (int i = 0; i < numHosts; i++) {
            List<Pe> peList = new ArrayList<>();
            for (int p = 0; p < hostPes; p++) {
                peList.add(new PeSimple(hostMips));
            }
            hostList.add(new HostSimple(hostRam, 10000, 1000000, peList));
        }
        new DatacenterSimple(simulation, hostList);

        var broker = new DatacenterBrokerSimple(simulation);

        var vmList = new ArrayList<VmSimple>();
        for (int i = 0; i < numVms; i++) {
            var vm = new VmSimple(hostMips, vmPes);
            vm.setRam(512).setBw(1000).setSize(10000);
            vmList.add(vm);
        }

        var cloudletList = new ArrayList<CloudletSimple>();
        var utilizationModel = new UtilizationModelDynamic(0.5);
        for (int i = 0; i < numCloudlets; i++) {
            var cloudlet = new CloudletSimple(cloudletLength, vmPes, utilizationModel);
            cloudlet.setSizes(1024);
            cloudletList.add(cloudlet);
        }

        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        simulation.start();

        new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();

        scanner.close();
    }
}