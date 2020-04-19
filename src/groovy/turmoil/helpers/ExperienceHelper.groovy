package turmoil.helpers

class ExperienceHelper
{
	public static Integer getRequiredExperience(Integer level)
	{
		return (ServerHelper.getExperiencePerLevelChart().containsKey(level)) ?
				ServerHelper.getExperiencePerLevelChart().get(level) : ServerHelper.getExperiencePerLevelChart().get(ServerHelper.maxLevel)
	}
}
