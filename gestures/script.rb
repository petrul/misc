#! /usr/bin/ruby

straight_left 	= 'FFFFFF'
bented_left 	= 'FFFFLFF'
cusped_left 	= 'FFFFFLLFLLLLFF'
pigtail_left 	= 'FFFFFLFLLFLLLLFFLLFLF'

gestures_left = [straight_left, bented_left, cusped_left, pigtail_left]

model = {
	0 => 'FFFFF',
	1 => 'LLFLLLLFF'
}

enc_straight_left = 'P0.F'
enc_bented_left = 'P0.DLFF'
enc_cusped_left = 'P0.P1.'
enc_pigtail_left = 'P0.LFP1.LLFLF'


def code_to_logo_program(code)
    puts "=" * 80
	puts 'reset'
	code.each_char do |c|
		case c
			when 'F'
				puts 'forward 20'
			when 'L' 
				puts 'turnleft 22.5'
		end
	end
end

gestures_left.each { |g| code_to_logo_program(g) }

