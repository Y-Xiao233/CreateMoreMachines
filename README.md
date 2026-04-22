<p align="center">
    <img width="2559" src="content.png" alt="title">  
</p>

# Create More Machines
- Added more tiers of machines to create
- Artist: [MHanHanBing](https://github.com/MHanHanBing)

### KubeJS compact
- Add new Tier for known advanced machines
```javascript
//startup_script
CMMEvent.registryTier(event =>{
    // id, itemCapability, fluidCapability, processingMultiple, deployerProcessingMultiple, mechanicalPressImpact, mechanicalMixerImpact, deployerImpact
    event.add("test_tier", 1024, 1600, 16, 8, 128, 64, 32)
})
```