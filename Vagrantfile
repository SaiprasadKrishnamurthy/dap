Vagrant.configure("2") do |config|
  config.vm.provision :shell, path: "shell.sh"
  config.vm.network "public_network", bridge: "Intel(R) 82579LM Gigabit Network Connection"

 
  config.vm.define "fedora1" do |fedora1|
    fedora1.vm.box = "fedora/23-cloud-base"
    fedora1.vm.network "private_network", ip: "192.168.122.10"
  end
 
  config.vm.define "fedora2" do |fedora2|
    fedora2.vm.box = "fedora/23-cloud-base"
    fedora2.vm.network "private_network", ip: "192.168.122.20"
  end
end